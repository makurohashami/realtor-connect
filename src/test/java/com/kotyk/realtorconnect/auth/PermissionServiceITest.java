package com.kotyk.realtorconnect.auth;

import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.realestate.RealEstatePhoto;
import com.kotyk.realtorconnect.entity.realestate.embedded.*;
import com.kotyk.realtorconnect.entity.realestate.enumeration.*;
import com.kotyk.realtorconnect.entity.realtor.Contact;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.entity.realtor.enumeration.ContactType;
import com.kotyk.realtorconnect.entity.realtor.enumeration.SubscriptionType;
import com.kotyk.realtorconnect.entity.user.Permission;
import com.kotyk.realtorconnect.entity.user.Role;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.repository.ContactRepository;
import com.kotyk.realtorconnect.repository.RealEstatePhotoRepository;
import com.kotyk.realtorconnect.repository.RealEstateRepository;
import com.kotyk.realtorconnect.repository.RealtorRepository;
import com.kotyk.realtorconnect.service.auth.PermissionService;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser("realtor")
public class PermissionServiceITest {

    @Autowired
    PermissionService permissionService;
    @Autowired
    RealtorRepository realtorRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    RealEstateRepository realEstateRepository;
    @Autowired
    RealEstatePhotoRepository realEstatePhotoRepository;

    Realtor user = Realtor.builder()
            .name("realtor")
            .email("realtor@mail.com")
            .username("realtor")
            .password(new BCryptPasswordEncoder().encode("realtor"))
            .phone("+380000000000")
            .role(Role.REALTOR)
            .subscriptionType(SubscriptionType.FREE)
            .blocked(false)
            .emailVerified(true)
            .build();

    Contact contact = Contact.builder()
            .type(ContactType.EMAIL)
            .contact("realtor@mail.com")
            .realtor(user)
            .build();

    RealEstate realEstate = RealEstate.builder()
            .name("name")
            .description("description")
            .price(BigDecimal.TEN)
            .verified(true)
            .owner(new Owner("name", "phone", "email"))
            .location(new Location("1", "1", "1", "1", "1", 1, "1", 1, "1"))
            .loggia(new Loggia(LoggiaType.LOGGIA, (short) 1, true))
            .bathroom(new Bathroom(BathroomType.TOILET_BATH, (short) 1, true))
            .area(new Area(1, 1, 1))
            .floor((short) 1)
            .floorsInBuilding((short) 1)
            .buildingType(BuildingType.APARTMENT)
            .heatingType(HeatingType.NO_HEATING)
            .windowsType(WindowsType.WOODEN)
            .hotWaterType(HotWaterType.NO_HOT_WATER)
            .stateType(StateType.LIVING)
            .announcementType(AnnouncementType.DAILY_RENT)
            .roomsCount((short) 1)
            .ceilingHeight(0)
            .documents("")
            .isPrivate(false)
            .called(true)
            .calledAt(Instant.now())
            .realtor(user)
            .build();

    RealEstatePhoto photo = RealEstatePhoto.builder()
            .photoId("photoId")
            .photo("photo")
            .order(0L)
            .isPrivate(false)
            .realEstate(realEstate)
            .build();

    @BeforeEach
    public void init(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();

        realtorRepository.save(user);
        contactRepository.save(contact);
        realEstateRepository.save(realEstate);
        realEstatePhotoRepository.save(photo);
    }

    @Test
    public void getUserSuccessTest() {
        //when
        Optional<User> optional = permissionService.getUser(user.getUsername());

        //then
        assertThat(optional, notNullValue());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get(), notNullValue());
        assertThat(optional.get().getId(), is(user.getId()));
    }

    @Test
    public void getUserErrorTest() {
        //when
        Optional<User> optional = permissionService.getUser(user.getUsername() + "-error");

        //then
        assertThat(optional, notNullValue());
        assertThat(optional.isPresent(), is(false));
    }

    @Test
    public void getContactSuccessTest() {
        //when
        Optional<Contact> optional = permissionService.getContact(contact.getId());

        //then
        assertThat(optional, notNullValue());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get(), notNullValue());
        assertThat(optional.get().getId(), is(contact.getId()));
    }

    @Test
    public void getContactErrorTest() {
        //when
        Optional<Contact> optional = permissionService.getContact(-1);

        //then
        assertThat(optional, notNullValue());
        assertThat(optional.isPresent(), is(false));
    }

    @Test
    public void getRealEstateSuccessTest() {
        //when
        Optional<RealEstate> optional = permissionService.getRealEstate(realEstate.getId());

        //then
        assertThat(optional, notNullValue());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get(), notNullValue());
        assertThat(optional.get().getId(), is(realEstate.getId()));
    }

    @Test
    public void getRealEstateErrorTest() {
        //when
        Optional<RealEstate> optional = permissionService.getRealEstate(-1);

        //then
        assertThat(optional, notNullValue());
        assertThat(optional.isPresent(), is(false));
    }

    @Test
    public void getRealEstatePhotoSuccessTest() {
        //when
        Optional<RealEstatePhoto> optional = permissionService.getPhoto(photo.getId());

        //then
        assertThat(optional, notNullValue());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get(), notNullValue());
        assertThat(optional.get().getId(), is(photo.getId()));
    }

    @Test
    public void getRealEstatePhotoErrorTest() {
        //when
        Optional<RealEstatePhoto> optional = permissionService.getPhoto(-1);

        //then
        assertThat(optional, notNullValue());
        assertThat(optional.isPresent(), is(false));
    }

    @Test
    @WithMockUser
    public void getCurrentUsernameSuccessTest() {
        //when
        String username = permissionService.getCurrentUsername();

        //then
        assertThat(username, notNullValue());
        assertThat(username, is("user"));
    }

    @Test
    @WithMockUser
    public void getCurrentUsernameErrorTest() {
        //when
        String username = permissionService.getCurrentUsername();

        //then
        assertThat(username, notNullValue());
        assertThat(username, is("user"));
        assertThat(username, is(not("another_user")));
    }

    @Test
    @WithMockUser(authorities = "SEE_PRIVATE_PHOTOS")
    public void isCurrentHasPermissionSuccessTest() {
        //when
        Boolean hasPermission = permissionService.isCurrentHasPermission(Permission.SEE_PRIVATE_PHOTOS);

        //then
        assertThat(hasPermission, notNullValue());
        assertThat(hasPermission, is(true));
    }

    @Test
    @WithMockUser(authorities = "SEE_PRIVATE_PHOTOS")
    public void isCurrentHasPermissionErrorTest() {
        //when
        Boolean hasPermission = permissionService.isCurrentHasPermission(Permission.MANAGE_ADMINS);

        //then
        assertThat(hasPermission, notNullValue());
        assertThat(hasPermission, is(false));
    }

    @Test
    public void isSameUserTrueTest() {
        //when
        Boolean result = permissionService.isSameUser(user.getId());

        //then
        assertThat(result, notNullValue());
        assertThat(result, is(true));
    }

    @Test
    @WithMockUser(username = "another_user")
    public void isSameUserFalseTest() {
        //when
        Boolean result = permissionService.isSameUser(user.getId());

        //then
        assertThat(result, notNullValue());
        assertThat(result, is(false));
    }

    @Test
    public void isContactOwnerTrueTest() {
        //when
        Boolean result = permissionService.isContactOwner(contact.getId());

        //then
        assertThat(result, notNullValue());
        assertThat(result, is(true));
    }

    @Test
    @WithMockUser(username = "another_user")
    public void isContactOwnerFalseTest() {
        //when
        Boolean result = permissionService.isContactOwner(contact.getId());

        //then
        assertThat(result, notNullValue());
        assertThat(result, is(false));
    }

    @Test
    public void isRealEstateOwnerTrueTest() {
        //when
        Boolean result = permissionService.isRealEstateOwner(realEstate.getId());

        //then
        assertThat(result, notNullValue());
        assertThat(result, is(true));
    }

    @Test
    @WithMockUser(username = "another_user")
    public void isRealEstateOwnerFalseTest() {
        //when
        Boolean result = permissionService.isRealEstateOwner(realEstate.getId());

        //then
        assertThat(result, notNullValue());
        assertThat(result, is(false));
    }

    @Test
    public void isRealEstatePhotoOwnerTrueTest() {
        //when
        Boolean result = permissionService.isRealEstatePhotoOwner(photo.getId());

        //then
        assertThat(result, notNullValue());
        assertThat(result, is(true));
    }

    @Test
    @WithMockUser(username = "another_user")
    public void isRealEstatePhotoOwnerFalseTest() {
        //when
        Boolean result = permissionService.isRealEstatePhotoOwner(photo.getId());

        //then
        assertThat(result, notNullValue());
        assertThat(result, is(false));
    }

    @Test
    public void isRealEstatePublicTrueTest() {
        //when
        Boolean result = permissionService.isRealEstatePublic(realEstate.getId());

        //then
        assertThat(result, notNullValue());
        assertThat(realEstate.isPrivate(), is(false));
        assertThat(result, is(true));
    }

    @Test
    public void isRealEstatePublicFalseTest() {
        //when
        Boolean result = permissionService.isRealEstatePublic(-1);

        //then
        assertThat(result, notNullValue());
        assertThat(result, is(false));
    }

}
