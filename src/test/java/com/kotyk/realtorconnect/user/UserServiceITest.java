package com.kotyk.realtorconnect.user;


import com.kotyk.realtorconnect.dto.user.UserAddDto;
import com.kotyk.realtorconnect.dto.user.UserDto;
import com.kotyk.realtorconnect.dto.user.UserFilter;
import com.kotyk.realtorconnect.dto.user.UserFullDto;
import com.kotyk.realtorconnect.entity.user.ConfirmationToken;
import com.kotyk.realtorconnect.entity.user.Role;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.repository.ConfirmationTokenRepository;
import com.kotyk.realtorconnect.repository.UserRepository;
import com.kotyk.realtorconnect.service.user.UserService;
import com.kotyk.realtorconnect.util.exception.ActionNotAllowedException;
import com.kotyk.realtorconnect.util.exception.ValidationFailedException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser
public class UserServiceITest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    User user1 = User.builder()
            .name("user1")
            .username("user1")
            .password("pass1")
            .email("email1")
            .blocked(false)
            .emailVerified(true)
            .role(Role.USER)
            .build();

    User user2 = User.builder()
            .name("user2")
            .username("user2")
            .password("pass2")
            .email("email2")
            .blocked(false)
            .emailVerified(true)
            .role(Role.REALTOR)
            .build();

    User user3 = User.builder()
            .name("user3")
            .username("user3")
            .password("pass3")
            .email("email3")
            .blocked(false)
            .emailVerified(true)
            .role(Role.USER)
            .build();

    @BeforeEach
    public void init(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();

        userRepository.saveAll(List.of(user1, user2, user3));
    }


    @Test
    public void createTest() {
        //given
        UserAddDto userToAdd = UserAddDto.builder()
                .name("userToAdd")
                .username("userToAdd")
                .password("userToAdd")
                .email("userToAdd")
                .build();
        long countBefore = userRepository.count();

        //when
        UserDto addedUser = userService.create(userToAdd, Role.USER);

        //then
        long countAfter = userRepository.count();
        assertThat(countAfter, is(countBefore + 1));

        assertThat(addedUser, notNullValue());
        assertThat(addedUser.getId(), notNullValue());
        assertThat(addedUser.getName(), is(userToAdd.getName()));
        assertThat(addedUser.getRole(), is(Role.USER));

        Optional<User> optional = userRepository.findById(addedUser.getId());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get().getName(), is(addedUser.getName()));
    }

    @Test
    public void findByIdTest() {
        //when
        User user = userService.findById(user1.getId());

        //then
        assertThat(user, notNullValue());
        assertThat(user, is(user1));
    }

    @Test
    public void readByIdTest() {
        //when
        UserDto user = userService.readById(user1.getId());

        //then
        assertThat(user, notNullValue());
        assertThat(user.getId(), is(user1.getId()));
        assertThat(user.getName(), is(user1.getName()));
    }

    @Test
    public void findByUsernameTest() {
        //when
        Optional<User> user = userService.findByUsername(user1.getUsername());

        //then
        assertThat(user, notNullValue());
        assertThat(user.isPresent(), is(true));
        assertThat(user.get(), is(user1));
    }

    @Test
    public void readFullByUsernameTest() {
        //when
        UserFullDto user = userService.readFullByUsername(user1.getUsername());

        //then
        assertThat(user, notNullValue());
        assertThat(user.getId(), is(user1.getId()));
        assertThat(user.getUsername(), is(user1.getUsername()));
    }

    @Test
    public void readFullByIdTest() {
        //when
        UserFullDto user = userService.readFullById(user1.getId());

        //then
        assertThat(user, notNullValue());
        assertThat(user.getId(), is(user1.getId()));
        assertThat(user.getUsername(), is(user1.getUsername()));
    }

    @Test
    public void readAllFullsPageTest() {
        //given
        Pageable paging = PageRequest.of(0, 10);
        UserFilter filter = UserFilter.builder().roles(List.of(Role.USER)).build();

        //when
        Page<UserFullDto> users = userService.readAllFulls(filter, paging);

        //then
        assertThat(users, notNullValue());
        assertThat(users.getNumberOfElements() > 0, is(true));
    }

    @Test
    public void readAllFullsListTest() {
        //given
        UserFilter filter = UserFilter.builder().roles(List.of(Role.REALTOR)).build();

        //when
        List<UserFullDto> users = userService.readAllFulls(filter);

        //then
        assertThat(users, notNullValue());
        assertThat(users.isEmpty(), is(false));
    }

    @Test
    public void verifyEmailTest() {
        //given
        boolean startVerified = user1.getEmailVerified();
        user1.setEmailVerified(false);
        ConfirmationToken token = confirmationTokenRepository.save(ConfirmationToken.builder().user(user1).build());
        long countTokensBefore = confirmationTokenRepository.count();

        //when
        userService.verifyEmail(token.getToken());

        //then
        long countTokensAfter = confirmationTokenRepository.count();
        assertThat(countTokensAfter, is(countTokensBefore - 1));
        assertThat(user1.getEmailVerified(), is(true));

        user1.setEmailVerified(startVerified);
    }

    @Test
    public void updateTest() {
        //given
        User userToUpdate = User.builder()
                .name("userToUpdate")
                .username("userToUpdate")
                .password("userToUpdate")
                .email("userToUpdate")
                .blocked(false)
                .emailVerified(true)
                .role(Role.USER)
                .build();
        userRepository.save(userToUpdate);
        UserAddDto newUserInfo = UserAddDto.builder()
                .name("newUserInfo")
                .username("newUserInfo")
                .password("newUserInfo")
                .email("newUserInfo")
                .build();
        long countBefore = userRepository.count();

        //when
        UserFullDto updatedUser = userService.update(userToUpdate.getId(), newUserInfo);

        //then
        long countAfter = userRepository.count();
        assertThat(countAfter, is(countBefore));

        assertThat(updatedUser, notNullValue());
        assertThat(updatedUser.getId(), notNullValue());
        assertThat(updatedUser.getName(), is(newUserInfo.getName()));

        Optional<User> optional = userRepository.findById(updatedUser.getId());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get().getName(), is(updatedUser.getName()));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void updateBlockedForUserTest() {
        //given
        User user = User.builder()
                .name("user")
                .username("user")
                .password("pass")
                .email("email")
                .blocked(false)
                .emailVerified(true)
                .role(Role.USER)
                .build();
        userRepository.save(user);

        //when
        Boolean isBlocked = userService.updateBlocked(user.getId(), true);

        //then
        assertThat(isBlocked, notNullValue());
        assertThat(isBlocked, is(true));

        Optional<User> optional = userRepository.findById(user.getId());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get().getBlocked(), is(true));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void updateBlockedForRealtorTest() {
        //given
        User user = User.builder()
                .name("user")
                .username("user")
                .password("pass")
                .email("email")
                .blocked(false)
                .emailVerified(true)
                .role(Role.REALTOR)
                .build();
        userRepository.save(user);

        //when
        Boolean isBlocked = userService.updateBlocked(user.getId(), true);

        //then
        assertThat(isBlocked, notNullValue());
        assertThat(isBlocked, is(true));

        Optional<User> optional = userRepository.findById(user.getId());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get().getBlocked(), is(true));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void updateBlockedForAdminTest() {
        //given
        User user = User.builder()
                .name("user")
                .username("user")
                .password("pass")
                .email("email")
                .blocked(false)
                .emailVerified(true)
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);

        //when
        ActionNotAllowedException exception = assertThrows(ActionNotAllowedException.class,
                () -> userService.updateBlocked(user.getId(), true));

        //then
        assertThat(exception.getMessage(), is("You cannot change 'blocked' for this user"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void updateBlockedForChiefAdminTest() {
        //given
        User user = User.builder()
                .name("user")
                .username("user")
                .password("pass")
                .email("email")
                .blocked(false)
                .emailVerified(true)
                .role(Role.CHIEF_ADMIN)
                .build();
        userRepository.save(user);

        //when
        ActionNotAllowedException exception = assertThrows(ActionNotAllowedException.class,
                () -> userService.updateBlocked(user.getId(), true));

        //then
        assertThat(exception.getMessage(), is("You cannot change 'blocked' for this user"));
    }

    @Test
    public void setAvatarTest() throws IOException {
        //given
        MockMultipartFile avatar = new MockMultipartFile("photo",
                "photo.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                getClass().getResourceAsStream("/files/photo.jpg")
        );
        User user = User.builder()
                .name("user")
                .username("user")
                .password("pass")
                .email("email")
                .blocked(false)
                .emailVerified(true)
                .role(Role.USER)
                .build();
        userRepository.save(user);

        //when
        String avatarUrl = userService.setAvatar(user.getId(), avatar);

        //then
        assertThat(avatarUrl, notNullValue());
    }

    @Test
    public void setAvatarWithNoPhotoTest() throws IOException {
        //given
        MockMultipartFile avatar = new MockMultipartFile("photo",
                "photo.txt",
                MediaType.TEXT_PLAIN_VALUE,
                getClass().getResourceAsStream("/files/photo.txt")
        );

        //when
        ValidationFailedException exception = assertThrows(ValidationFailedException.class,
                () -> userService.setAvatar(user1.getId(), avatar));

        //then
        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), notNullValue());
        assertThat(exception.getMessage().startsWith("Avatar not valid"), is(true));
    }

    @Test
    public void deleteAvatarTest() {
        //given
        User user = User.builder()
                .name("user")
                .username("user")
                .password("user")
                .email("user")
                .blocked(false)
                .emailVerified(true)
                .role(Role.USER)
                .build();
        userRepository.save(user);

        //when
        userService.deleteAvatar(user.getId());

        //then
        assertThat(user.getAvatarId(), nullValue());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void deleteUserTest() {
        //given
        User user = User.builder()
                .name("user")
                .username("user")
                .password("user")
                .email("user")
                .blocked(false)
                .emailVerified(true)
                .role(Role.USER)
                .build();
        userRepository.save(user);
        long countBefore = userRepository.count();

        //when
        userService.delete(user.getId());

        //then
        long countAfter = userRepository.count();
        assertThat(countAfter, is(countBefore - 1));

        Optional<User> optional = userRepository.findById(user.getId());
        assertThat(optional.isPresent(), is(false));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void deleteRealtorTest() {
        //given
        User user = User.builder()
                .name("user")
                .username("user")
                .password("user")
                .email("user")
                .blocked(false)
                .emailVerified(true)
                .role(Role.REALTOR)
                .build();
        userRepository.save(user);
        long countBefore = userRepository.count();

        //when
        userService.delete(user.getId());

        //then
        long countAfter = userRepository.count();
        assertThat(countAfter, is(countBefore - 1));

        Optional<User> optional = userRepository.findById(user.getId());
        assertThat(optional.isPresent(), is(false));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @WithMockUser(authorities = "MANAGE_ADMINS")
    public void deleteAdminWithPermissionTest() {
        //given
        User user = User.builder()
                .name("user")
                .username("user")
                .password("user")
                .email("user")
                .blocked(false)
                .emailVerified(true)
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);
        long countBefore = userRepository.count();

        //when
        userService.delete(user.getId());

        //then
        long countAfter = userRepository.count();
        assertThat(countAfter, is(countBefore - 1));

        Optional<User> optional = userRepository.findById(user.getId());
        assertThat(optional.isPresent(), is(false));
    }

    @Test
    public void deleteAdminWithoutPermissionTest() {
        //given
        User user = User.builder()
                .name("user")
                .username("user")
                .password("user")
                .email("user")
                .blocked(false)
                .emailVerified(true)
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);
        long countBefore = userRepository.count();

        //when
        ActionNotAllowedException exception = assertThrows(ActionNotAllowedException.class, () -> userService.delete(user.getId()));

        //then
        long countAfter = userRepository.count();
        assertThat(countAfter, is(countBefore));
        assertThat(exception.getMessage(), notNullValue());
    }

    @Test
    public void deleteChiefAdminTest() {
        //given
        User user = User.builder()
                .name("user")
                .username("user")
                .password("user")
                .email("user")
                .blocked(false)
                .emailVerified(true)
                .role(Role.CHIEF_ADMIN)
                .build();
        userRepository.save(user);
        long countBefore = userRepository.count();

        //when
        ActionNotAllowedException exception = assertThrows(ActionNotAllowedException.class, () -> userService.delete(user.getId()));

        //then
        long countAfter = userRepository.count();
        assertThat(countAfter, is(countBefore));
        assertThat(exception.getMessage(), notNullValue());
    }

}
