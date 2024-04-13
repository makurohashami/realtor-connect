package com.kotyk.realtorconnect.service.user;

import com.kotyk.realtorconnect.annotation.Loggable;
import com.kotyk.realtorconnect.config.UserConfiguration;
import com.kotyk.realtorconnect.entity.user.ConfirmationToken;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.repository.ConfirmationTokenRepository;
import com.kotyk.realtorconnect.util.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
@Loggable
@AllArgsConstructor
public class ConfirmationTokenService {

    private final UserConfiguration userConfiguration;
    private final ConfirmationTokenRepository tokenRepository;

    @Transactional
    public UUID createToken(User user) {
        return tokenRepository
                .save(ConfirmationToken.builder().user(user).build())
                .getToken();
    }

    @Transactional(readOnly = true)
    public User findUserByToken(UUID token) {
        return tokenRepository.findById(token)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found by token: '%s'", token)))
                .getUser();
    }

    @Transactional
    public void deleteToken(UUID token) {
        tokenRepository.deleteById(token);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        tokenRepository.deleteByUserId(userId);
    }

    @Transactional
    @Scheduled(cron = "${user.scheduler.delete-unused-tokens-cron}")
    protected void deleteOldTokens() {
        Instant time = ZonedDateTime.now()
                .minusDays(userConfiguration.getTokenTtlInDays())
                .toInstant();
        tokenRepository.deleteAllByCreatedAtBefore(time);
    }

}
