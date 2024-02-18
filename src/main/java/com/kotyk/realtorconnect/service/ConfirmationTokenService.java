package com.kotyk.realtorconnect.service;

import com.kotyk.realtorconnect.entity.user.ConfirmationToken;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.repository.ConfirmationTokenRepository;
import com.kotyk.realtorconnect.util.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository tokenRepository;

    @Transactional
    public String createToken(User user) {
        return tokenRepository.save(ConfirmationToken.builder().user(user).build())
                .getToken()
                .toString();
    }

    @Transactional(readOnly = true)
    public User getUserByToken(String token) {
        return tokenRepository.findById(UUID.fromString(token))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found by token: '%s'", token)))
                .getUser();
    }

    @Transactional
    public void deleteToken(String uuid) {
        tokenRepository.deleteById(UUID.fromString(uuid));
    }

}
