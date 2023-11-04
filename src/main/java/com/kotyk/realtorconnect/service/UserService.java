package com.kotyk.realtorconnect.service;

import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateLastLogin(User user) {
        log.debug("updateLastLogin() - start: user = {}", user);
        user.setLastLogin(Instant.now());
        userRepository.save(user);
        log.debug("updateLastLogin() - end: last login = {}", user.getLastLogin());
    }

    public Optional<User> findByUsername(String username) {
        log.debug("findByUsername() - start: username = {}", username);
        Optional<User> toReturn = userRepository.findByUsername(username);
        log.debug("findByUsername() - end: returned = {}", toReturn);
        return toReturn;
    }

}
