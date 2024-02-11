package com.kotyk.realtorconnect.service;

import com.kotyk.realtorconnect.entity.user.ConfirmationToken;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.repository.ConfirmationTokenRepository;
import com.kotyk.realtorconnect.util.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository tokenRepository;

    public String createToken(User user) {
        log.debug("createToken() - start. user - {}", user);
        ConfirmationToken token = tokenRepository.save(ConfirmationToken.builder().user(user).build());
        log.debug("createToken() - end. result = {}", token);
        return token.getToken().toString();
    }

    public User getUserByToken(String token) {
        log.debug("getUser() - start. token - {}", token);
        User user = tokenRepository.findById(UUID.fromString(token))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found by token: '%s'", token)))
                .getUser();
        log.debug("getUser() - end. result = {}", user);
        return user;
    }

    public void deleteToken(String uuid) {
        log.debug("deleteToken() - start. uuid - {}", uuid);
        tokenRepository.deleteById(UUID.fromString(uuid));
        log.debug("deleteToken() - end.");
    }

}
