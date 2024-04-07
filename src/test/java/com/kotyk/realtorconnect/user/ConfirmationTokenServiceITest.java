package com.kotyk.realtorconnect.user;

import com.kotyk.realtorconnect.entity.user.ConfirmationToken;
import com.kotyk.realtorconnect.entity.user.Role;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.repository.ConfirmationTokenRepository;
import com.kotyk.realtorconnect.repository.UserRepository;
import com.kotyk.realtorconnect.service.user.ConfirmationTokenService;
import com.kotyk.realtorconnect.util.exception.ResourceNotFoundException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser
public class ConfirmationTokenServiceITest {

    @Autowired
    ConfirmationTokenService confirmationTokenService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    User user = User.builder()
            .name("user")
            .username("user")
            .password(new BCryptPasswordEncoder().encode("pass"))
            .email("email")
            .blocked(false)
            .emailVerified(false)
            .role(Role.USER)
            .build();

    ConfirmationToken token = ConfirmationToken.builder()
            .user(user)
            .build();

    @BeforeEach
    public void init(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();

        userRepository.save(user);
        confirmationTokenRepository.save(token);
    }

    @Test
    public void createTokenTest() {
        //given
        User userToCreate = User.builder()
                .name("newUser")
                .username("newUser")
                .password(new BCryptPasswordEncoder().encode("newUser"))
                .email("newUserEmail")
                .blocked(false)
                .emailVerified(false)
                .role(Role.USER)
                .build();
        userRepository.save(userToCreate);
        long countBefore = confirmationTokenRepository.count();

        //when
        UUID token = confirmationTokenService.createToken(userToCreate);

        //then
        long countAfter = confirmationTokenRepository.count();
        assertThat(countAfter, is(not(countBefore)));
        assertThat(countAfter, is(countBefore + 1));

        assertThat(token, notNullValue());

        Optional<ConfirmationToken> optional = confirmationTokenRepository.findById(token);
        assertThat(optional, notNullValue());
        assertThat(optional.isPresent(), is(true));
        assertThat(token, is(optional.get().getToken()));
    }

    @Test
    public void getUserByTokenTest() {
        //when
        User userFromService = confirmationTokenService.getUserByToken(token.getToken());

        //then
        assertThat(userFromService, notNullValue());
        assertThat(userFromService.getId(), is(user.getId()));
    }

    @Test
    public void getUserByTokenWithErrorTest() {
        //given
        UUID uuid = UUID.randomUUID();

        //when
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> confirmationTokenService.getUserByToken(uuid)
        );

        //then
        assertThat(exception, notNullValue());
        assertThat(exception, instanceOf(ResourceNotFoundException.class));
        assertThat(exception.getMessage(), notNullValue());
    }

    @Test
    public void deleteTokenTest() {
        //given
        long countBefore = confirmationTokenRepository.count();

        //when
        confirmationTokenService.deleteToken(token.getToken());

        //then
        long countAfter = confirmationTokenRepository.count();
        assertThat(countAfter, is(not(countBefore)));
        assertThat(countAfter, is(countBefore - 1));

        Optional<ConfirmationToken> optional = confirmationTokenRepository.findById(token.getToken());
        assertThat(optional, notNullValue());
        assertThat(optional.isPresent(), is(false));
    }

}
