package com.kotyk.realtorconnect.service.user;

import com.kotyk.realtorconnect.config.UserConfiguration;
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
import com.kotyk.realtorconnect.specification.UserFilterSpecifications;
import com.kotyk.realtorconnect.util.exception.ActionNotAllowedException;
import com.kotyk.realtorconnect.util.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.kotyk.realtorconnect.entity.user.Role.ADMIN;
import static com.kotyk.realtorconnect.entity.user.Role.CHIEF_ADMIN;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private static final String NOT_FOUND_BY_ID_MSG = "User with id '%d' not found";
    private static final String NOT_FOUND_BY_USERNAME_MSG = "User with username '%s' not found";

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final UserConfiguration userConfiguration;
    private final EmailFacade emailFacade;
    private final ConfirmationTokenService tokenService;
    private final PermissionService permissionService;

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
    public UserDto readById(long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id))));
    }

    @Transactional(readOnly = true)
    public UserFullDto readFullById(long id) {
        return userMapper.toFullDto(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id))));
    }

    @Transactional(readOnly = true)
    public UserFullDto readFullByUsername(String username) {
        return userMapper.toFullDto(userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_USERNAME_MSG, username))));
    }

    @Transactional(readOnly = true)
    public Page<UserFullDto> readAllFulls(UserFilter filter, Pageable pageable) {
        Specification<User> spec = UserFilterSpecifications.withFilter(filter);
        return userRepository.findAll(spec, pageable).map(userMapper::toFullDto);
    }

    @Transactional(readOnly = true)
    public List<UserFullDto> readAllFulls(UserFilter filter) {
        Specification<User> spec = UserFilterSpecifications.withFilter(filter);
        return userMapper.toListFullDto(userRepository.findAll(spec));
    }

    @Transactional
    public UserFullDto update(long id, UserAddDto dto) {
        User toUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id)));
        return userMapper.toFullDto(userMapper.update(toUpdate, dto));
    }

    @Transactional
    public void delete(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id)));
        boolean canDeleteAdmins = permissionService.isCurrentHasPermission(Permission.MANAGE_ADMINS);
        if (user.getRole() == CHIEF_ADMIN || (user.getRole() == ADMIN && !canDeleteAdmins)) {
            throw new ActionNotAllowedException("You can't delete an user with this role");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public boolean updateBlocked(long id, boolean blocked) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id)));
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

}
