package com.kotyk.realtorconnect.repository;

import com.kotyk.realtorconnect.entity.user.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, UUID> {

    void deleteByUserId(Long userId);

    Optional<ConfirmationToken> findByUserId(Long userId);

    void deleteAllByCreatedAtBefore(Instant createdAt);

}
