package com.kotyk.realtorconnect.auth;

import com.kotyk.realtorconnect.dto.auth.JwtToken;
import com.kotyk.realtorconnect.entity.user.Role;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.repository.UserRepository;
import com.kotyk.realtorconnect.service.auth.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithAnonymousUser
public class JwtServiceITest {

    @Autowired
    JwtService jwtService;
    @Autowired
    UserRepository userRepository;

    User user = User.builder()
            .name("user")
            .email("user@mail.com")
            .username("user")
            .password(new BCryptPasswordEncoder().encode("pass"))
            .phone("+380000000000")
            .role(Role.REALTOR)
            .blocked(false)
            .emailVerified(true)
            .build();

    @Test
    public void generateTokenTest() {
        //when
        String token = jwtService.generateToken(user);

        //then
        assertThat(token, notNullValue());
    }

    @Test
    public void parseTokenTest() {
        //when
        JwtToken token = jwtService.parseToken(jwtService.generateToken(user));

        //when
        assertThat(token, notNullValue());
        assertThat(token.getExpiration().isAfter(Instant.now()), is(true));
        assertThat(token.getRole(), is(user.getRole()));
        assertThat(token.getUsername(), is(user.getUsername()));
    }

    @Test
    public void parseTokenWithInvalidTokenTest() {
        //when
        Exception asserted = assertThrows(Exception.class, () -> jwtService.parseToken(""));

        //then
        assertThat(asserted, notNullValue());
        assertThat(asserted.getMessage(), notNullValue());
    }

    @Test
    public void isTokenValidTest() {
        //when
        Boolean isValid = jwtService.isTokenValid(jwtService.parseToken(jwtService.generateToken(user)));

        //then
        assertThat(isValid, notNullValue());
        assertThat(isValid, is(true));
    }

}
