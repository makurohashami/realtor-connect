package com.kotyk.realtorconnect.service.user;

import com.kotyk.realtorconnect.config.UserConfiguration;
import com.kotyk.realtorconnect.dto.file.FileUploadResponse;
import com.kotyk.realtorconnect.dto.user.UserAddDto;
import com.kotyk.realtorconnect.dto.user.UserDto;
import com.kotyk.realtorconnect.dto.user.UserFilter;
import com.kotyk.realtorconnect.dto.user.UserFullDto;
import com.kotyk.realtorconnect.entity.user.Permission;
import com.kotyk.realtorconnect.entity.user.Role;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.mapper.UserMapper;
import com.kotyk.realtorconnect.repository.UserRepository;
import com.kotyk.realtorconnect.service.auth.PermissionService;
import com.kotyk.realtorconnect.service.email.EmailFacade;
import com.kotyk.realtorconnect.service.file.FileParamsGenerator;
import com.kotyk.realtorconnect.service.file.FileUploaderService;
import com.kotyk.realtorconnect.specification.UserFilterSpecifications;
import com.kotyk.realtorconnect.util.exception.ActionNotAllowedException;
import com.kotyk.realtorconnect.util.exception.ResourceNotFoundException;
import com.kotyk.realtorconnect.util.exception.ValidationFailedException;
import com.kotyk.realtorconnect.util.validator.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.kotyk.realtorconnect.entity.user.Role.ADMIN;
import static com.kotyk.realtorconnect.entity.user.Role.CHIEF_ADMIN;

@Slf4j
@Service
@AllArgsConstructor
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService {

    private static final String NOT_FOUND_BY_ID_MSG = "User with id '%d' not found";
    private static final String NOT_FOUND_BY_USERNAME_MSG = "User with username '%s' not found";

    private final UserService proxy;

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserConfiguration userConfiguration;
    private final EmailFacade emailFacade;
    private final ConfirmationTokenService tokenService;
    private final PermissionService permissionService;
    private final Validator<MultipartFile> avatarValidator;
    private final FileParamsGenerator fileParamsGenerator;
    private final FileUploaderService fileUploaderService;

    @Transactional
    public void updateLastLogin(User user) {
        user.setLastLogin(Instant.now());
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public UserFullDto create(UserAddDto dto, Role role) {
        User user = userMapper.toEntity(dto);
        user.setRole(role);
        UserFullDto userFullDto = userMapper.toFullDto(userRepository.save(user));
        emailFacade.sendVerifyEmail(user, tokenService.createToken(user));
        return userFullDto;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "getUser", key = "#id")
    public User findById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id)));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "getUserDto", key = "#id")
    public UserDto readById(long id) {
        return userMapper.toDto(findById(id));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "getUserFullDto", key = "#id")
    public UserFullDto readFullById(long id) {
        return userMapper.toFullDto(findById(id));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "getUserFullDto", key = "#username")
    public UserFullDto readFullByUsername(String username) {
        return userMapper.toFullDto(userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_USERNAME_MSG, username))));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "getAllUserFullDto", key = "#filter+'-'+#pageable")
    public Page<UserFullDto> readAllFulls(UserFilter filter, Pageable pageable) {
        Specification<User> spec = UserFilterSpecifications.withFilter(filter);
        return userRepository.findAll(spec, pageable).map(userMapper::toFullDto);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "getAllUserFullDto", key = "#filter")
    public List<UserFullDto> readAllFulls(UserFilter filter) {
        Specification<User> spec = UserFilterSpecifications.withFilter(filter);
        return userMapper.toListFullDto(userRepository.findAll(spec));
    }

    @Transactional
    public UserFullDto update(long id, UserAddDto dto) {
        User toUpdate = proxy.findById(id);
        return userMapper.toFullDto(userMapper.update(toUpdate, dto));
    }

    @Transactional
    public void delete(long id) {
        User user = proxy.findById(id);
        boolean canDeleteAdmins = permissionService.isCurrentHasPermission(Permission.MANAGE_ADMINS);
        if (user.getRole() == CHIEF_ADMIN || (user.getRole() == ADMIN && !canDeleteAdmins)) {
            throw new ActionNotAllowedException("You can't delete an user with this role");
        }

        deleteAvatar(id);
        userRepository.deleteById(id);
    }

    @Transactional
    public boolean updateBlocked(long id, boolean blocked) {
        User user = proxy.findById(id);
        if (user.getRole() == ADMIN || user.getRole() == CHIEF_ADMIN) {
            throw new ActionNotAllowedException("You cannot change 'blocked' for this user");
        }
        user.setBlocked(blocked);
        return user.getBlocked();
    }

    @Transactional
    public boolean verifyEmail(String token) {
        User user = tokenService.getUserByToken(token);
        user.setEmailVerified(true);
        tokenService.deleteToken(token);
        return user.getEmailVerified();
    }

    @Transactional
    @Scheduled(cron = "${user.scheduler.delete-unverified-users-cron}")
    protected void deleteUnverifiedUsers() {
        Instant time = ZonedDateTime.now()
                .minusDays(userConfiguration.getTimeToVerifyEmailInDays())
                .toInstant();
        userRepository.deleteAllByCreatedIsBeforeAndEmailVerifiedFalse(time);
    }

    @Transactional
    public String setAvatar(long id, MultipartFile avatar) {
        User user = proxy.findById(id);
        validateAvatar(avatar);
        Map<String, Object> params = fileParamsGenerator.generateParamsForAvatar(user);
        FileUploadResponse response = fileUploaderService.uploadFile(avatar, params);
        mapAvatarToUser(user, response);
        return user.getAvatar();
    }

    @Transactional
    public void deleteAvatar(long id) {
        User user = proxy.findById(id);
        fileUploaderService.deleteFile(user.getAvatarId());
        user.setAvatar(userMapper.getDefaultAvatarUrl());
    }

    private void validateAvatar(MultipartFile avatar) {
        List<String> errors = avatarValidator.validate(avatar);
        if (!CollectionUtils.isEmpty(errors)) {
            throw new ValidationFailedException("Avatar not valid, errors: " + String.join("; ", errors));
        }
    }

    private void mapAvatarToUser(User user, FileUploadResponse response) {
        user.setAvatar(response.getFileId() == null ? userMapper.getDefaultAvatarUrl() : response.getUrl());
        user.setAvatarId(response.getFileId());
    }
}
