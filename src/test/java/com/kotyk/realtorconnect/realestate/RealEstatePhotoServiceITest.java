package com.kotyk.realtorconnect.realestate;

import com.kotyk.realtorconnect.dto.realestate.photo.RealEstatePhotoDto;
import com.kotyk.realtorconnect.dto.realestate.photo.RealEstatePhotoUpdateDto;
import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.realestate.RealEstatePhoto;
import com.kotyk.realtorconnect.entity.realestate.embedded.*;
import com.kotyk.realtorconnect.entity.realestate.enumeration.*;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.entity.realtor.enumeration.SubscriptionType;
import com.kotyk.realtorconnect.entity.user.Role;
import com.kotyk.realtorconnect.repository.RealEstatePhotoRepository;
import com.kotyk.realtorconnect.repository.RealEstateRepository;
import com.kotyk.realtorconnect.repository.RealtorRepository;
import com.kotyk.realtorconnect.service.realestate.RealEstatePhotoService;
import com.kotyk.realtorconnect.util.exception.ActionNotAllowedException;
import com.kotyk.realtorconnect.util.exception.ValidationFailedException;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(roles = "REALTOR")
public class RealEstatePhotoServiceITest {

    @Autowired
    RealEstatePhotoService realEstatePhotoService;
    @Autowired
    RealEstatePhotoRepository realEstatePhotoRepository;
    @Autowired
    RealEstateRepository realEstateRepository;
    @Autowired
    RealtorRepository realtorRepository;

    Realtor realtor1 = Realtor.builder()
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

    RealEstate realEstate1 = RealEstate.builder()
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
            .realtor(realtor1)
            .build();

    RealEstatePhoto photo1 = RealEstatePhoto.builder()
            .photo("photo1")
            .photoId("photoId1")
            .isPrivate(false)
            .order(2L)
            .realEstate(realEstate1)
            .build();

    RealEstatePhoto photo2 = RealEstatePhoto.builder()
            .photo("photo2")
            .photoId("photoId2")
            .isPrivate(false)
            .order(5L)
            .realEstate(realEstate1)
            .build();

    @BeforeEach
    public void init(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();

        realtorRepository.save(realtor1);
        realEstateRepository.save(realEstate1);
        realEstatePhotoRepository.save(photo1);
        realEstatePhotoRepository.save(photo2);
    }

    @Test
    public void createTest() throws IOException {
        //given
        MockMultipartFile photo = new MockMultipartFile("photo",
                "photo.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                getClass().getResourceAsStream("/files/photo.jpg")
        );
        long countBefore = realEstatePhotoRepository.count();

        //when
        List<RealEstatePhotoDto> photos = realEstatePhotoService.create(realEstate1.getId(), Set.of(photo));

        //then
        long countAfter = realEstatePhotoRepository.count();
        assertThat(countAfter, is(countBefore + 1));

        assertThat(photos, notNullValue());
        assertThat(photos.isEmpty(), is(false));
    }

    @Test
    public void createWithNoPhotoTest() throws IOException {
        //given
        MockMultipartFile photo = new MockMultipartFile("photo",
                "photo.txt",
                MediaType.TEXT_PLAIN_VALUE,
                getClass().getResourceAsStream("/files/photo.txt")
        );

        //when
        ValidationFailedException exception = assertThrows(ValidationFailedException.class,
                () -> realEstatePhotoService.create(realEstate1.getId(), Set.of(photo)));

        //then
        assertThat(exception.getMessage(), notNullValue());
        assertThat(exception.getMessage().startsWith("Some photos are not valid"), is(true));
    }

    @Test
    public void readAllTest() {
        //when
        List<RealEstatePhotoDto> photos = realEstatePhotoService.readAll(realEstate1.getId());

        //then
        assertThat(photos, notNullValue());
        assertThat(photos.isEmpty(), is(false));
    }

    @Test
    public void updateTest() {
        //given
        RealEstatePhoto toUpdate = RealEstatePhoto.builder()
                .photo("toUpdate")
                .photoId("toUpdate")
                .isPrivate(false)
                .order(5L)
                .realEstate(realEstate1)
                .build();
        realEstatePhotoRepository.save(toUpdate);
        RealEstatePhotoUpdateDto newData = RealEstatePhotoUpdateDto.builder().isPrivate(true).build();
        long countBefore = realEstatePhotoRepository.count();

        //when
        RealEstatePhotoDto updated = realEstatePhotoService.update(toUpdate.getId(), newData);

        //then
        long countAfter = realEstatePhotoRepository.count();
        assertThat(countAfter, is(countBefore));

        assertThat(updated, notNullValue());
        assertThat(updated.isPrivate(), is(newData.isPrivate()));


        Optional<RealEstatePhoto> optional = realEstatePhotoRepository.findById(toUpdate.getId());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get().isPrivate(), is(newData.isPrivate()));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void customiseOrderTest() {
        //given
        LinkedHashSet<Long> ids = new LinkedHashSet<>();
        ids.add(photo2.getId());
        ids.add(photo1.getId());

        //when
        realEstatePhotoService.customiseOrder(realEstate1.getId(), ids);

        //then
        List<RealEstatePhoto> photos = realEstatePhotoRepository.findAll(Sort.by(Sort.Direction.ASC, "order"));
        assertThat(photos.get(0).getId(), is(photo2.getId()));
        assertThat(photos.get(1).getId(), is(photo1.getId()));
        assertThat(photos.get(0).getOrder() < photos.get(1).getOrder(), is(true));
    }

    @Test
    public void customiseOrderWithWrongIdsTest() {
        //given
        LinkedHashSet<Long> ids = new LinkedHashSet<>();
        ids.add(-1L);
        ids.add(-2L);

        //when
        ActionNotAllowedException exception = assertThrows(ActionNotAllowedException.class,
                () -> realEstatePhotoService.customiseOrder(realEstate1.getId(), ids));

        //then
        assertThat(exception.getMessage(), notNullValue());
        assertThat(exception.getMessage(), is("The count of IDs does not match the actual count of IDs or the IDs do not match"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void validateOrderTest() {
        //when
        realEstatePhotoService.validateOrder(realEstate1.getId());

        //then
        List<RealEstatePhoto> photos = realEstatePhotoRepository.findAll(Sort.by(Sort.Direction.ASC, "order"));
        assertThat(photos.get(0).getId(), is(photo1.getId()));
        assertThat(photos.get(1).getId(), is(photo2.getId()));
        assertThat(photos.get(0).getOrder() < photos.get(1).getOrder(), is(true));
    }

    @Test
    public void deleteTest() {
        //given
        long countBefore = realEstatePhotoRepository.count();

        //when
        realEstatePhotoService.delete(photo1.getId());

        //then
        long countAfter = realEstatePhotoRepository.count();
        assertThat(countAfter, is(countBefore - 1));

        Optional<RealEstatePhoto> optional = realEstatePhotoRepository.findById(photo1.getId());
        assertThat(optional.isPresent(), is(false));
    }

}
