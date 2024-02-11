package com.kotyk.realtorconnect.repository;

import com.kotyk.realtorconnect.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    List<User> findAll(Specification<User> spec);

    void deleteAllByCreatedIsBeforeAndEmailVerifiedFalse(Instant time);

}
