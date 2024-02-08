package com.kotyk.realtorconnect.service;

import com.kotyk.realtorconnect.dto.user.UserAddDto;
import com.kotyk.realtorconnect.dto.user.UserDto;
import com.kotyk.realtorconnect.dto.user.UserFilter;
import com.kotyk.realtorconnect.entity.user.Role;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.mapper.UserMapper;
import com.kotyk.realtorconnect.repository.UserRepository;
import com.kotyk.realtorconnect.service.email.EmailFacade;
import com.kotyk.realtorconnect.specification.UserFilterSpecifications;
import com.kotyk.realtorconnect.util.exception.ActionNotAllowedException;
import com.kotyk.realtorconnect.util.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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
    private final EmailFacade emailFacade;

    @Transactional
    public void updateLastLogin(User user) {
        log.debug("updateLastLogin() - start. user = {}", user);
        user.setLastLogin(Instant.now());
        userRepository.save(user);
        log.debug("updateLastLogin() - end. last login = {}", user.getLastLogin());
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        log.debug("findByUsername() - start. username = {}", username);
        Optional<User> toReturn = userRepository.findByUsername(username);
        log.debug("findByUsername() - end. returned = {}", toReturn);
        return toReturn;
    }

    @Transactional
    public UserDto create(UserAddDto dto, Role role) {
        log.debug("create() - start. role = {}, dto = {}", role, dto);
        User user = userMapper.toEntity(dto);
        user.setRole(role);
        UserDto saved = userMapper.toDto(userRepository.save(user));
        emailFacade.sendVerifyEmail(user);
        log.debug("create() - end. saved = {}", saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public UserDto readById(long id) {
        log.debug("readById() - start. id = {}", id);
        UserDto dto = userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id))));
        log.debug("readById() - end. user = {}", dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public UserDto readByUsername(String username) {
        log.debug("readByUsername() - start. username = {}", username);
        UserDto dto = userMapper.toDto(userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_USERNAME_MSG, username))));
        log.debug("readByUsername() - end. user = {}", dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public Page<UserDto> readAll(UserFilter filter, Pageable pageable) {
        log.debug("readAll() - start. filter = {}, pageable = {}", filter, pageable);
        Specification<User> spec = UserFilterSpecifications.withFilter(filter);
        Page<UserDto> users = userRepository.findAll(spec, pageable).map(userMapper::toDto);
        log.debug("readAll() - end: size = {}", users.getTotalElements());
        return users;
    }

    @Transactional(readOnly = true)
    public List<UserDto> readAll(UserFilter filter) {
        log.debug("readAll() - start. filter = {}", filter);
        Specification<User> spec = UserFilterSpecifications.withFilter(filter);
        List<UserDto> users = userMapper.toListDto(userRepository.findAll(spec));
        log.debug("readAll() - end: size = {}", users.size());
        return users;
    }

    @Transactional
    public UserDto update(long id, UserAddDto dto) {
        log.debug("update() - start. id = {}, dto = {}", id, dto);
        User toUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id)));
        UserDto updated = userMapper.toDto(userMapper.update(toUpdate, dto));
        log.debug("update() - end. result = {}", updated);
        return updated;
    }

    @Transactional
    public void delete(long id) {
        log.debug("delete() - start. id = {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id)));
        if (user.getRole() == ADMIN || user.getRole() == CHIEF_ADMIN) {
            throw new ActionNotAllowedException("You can't delete an user with this role");
        }
        userRepository.deleteById(id);
        log.debug("delete() - end. deleted");
    }

    @Transactional
    public void deleteAdmin(long id) {
        log.debug("deleteAdmin() - start. id = {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id)));
        if (user.getRole() != ADMIN) {
            throw new ActionNotAllowedException("You can't delete an user with this role");
        }
        userRepository.deleteById(id);
        log.debug("deleteAdmin() - end. deleted");
    }

    @Transactional
    public boolean updateBlocked(long id, boolean blocked) {
        log.debug("updateBlocked() - start. id = {}, blocked = {}", id, blocked);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id)));
        if (user.getRole() == ADMIN || user.getRole() == CHIEF_ADMIN) {
            throw new ActionNotAllowedException("You cannot change 'blocked' for this user");
        }
        user.setBlocked(blocked);
        log.debug("updateBlocked() - end. blocked = {}", user.getBlocked());
        return user.getBlocked();
    }

    @Transactional
    public boolean verifyEmail(String username) {
        log.debug("verifyEmail() - start. username = {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_USERNAME_MSG, username)));
        user.setEmailVerified(true);
        log.debug("verifyEmail() - end. email verified = {}", user.getEmailVerified());
        return user.getEmailVerified();
    }
}
