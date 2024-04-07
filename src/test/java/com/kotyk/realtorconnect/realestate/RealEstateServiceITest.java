package com.kotyk.realtorconnect.realestate;

import com.kotyk.realtorconnect.dto.realestate.RealEstateAddDto;
import com.kotyk.realtorconnect.dto.realestate.RealEstateDto;
import com.kotyk.realtorconnect.dto.realestate.RealEstateFilter;
import com.kotyk.realtorconnect.dto.realestate.RealEstateFullDto;
import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.realestate.embedded.*;
import com.kotyk.realtorconnect.entity.realestate.enumeration.*;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.entity.realtor.enumeration.SubscriptionType;
import com.kotyk.realtorconnect.entity.user.Role;
import com.kotyk.realtorconnect.repository.RealEstateRepository;
import com.kotyk.realtorconnect.repository.RealtorRepository;
import com.kotyk.realtorconnect.service.realestate.RealEstateService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser(roles = "REALTOR")
public class RealEstateServiceITest {

    @Autowired
    RealEstateService realEstateService;
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

    @BeforeEach
    public void init(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();

        realtorRepository.save(realtor1);
        realEstateRepository.save(realEstate1);
    }

    @Test
    public void createTest() {
        //given
        RealEstateAddDto toAdd = RealEstateAddDto.builder()
                .name("name")
                .description("description")
                .price(BigDecimal.TEN)
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
                .build();
        long countBefore = realEstateRepository.count();

        //when
        RealEstateFullDto created = realEstateService.create(realtor1.getId(), toAdd);

        //then
        long countAfter = realEstateRepository.count();
        assertThat(countAfter, is(countBefore + 1));

        assertThat(created, notNullValue());
        assertThat(created.getName(), is(toAdd.getName()));
        assertThat(created.isPrivate(), is(toAdd.isPrivate()));
        assertThat(created.getOwner(), is(toAdd.getOwner()));
    }

    @Test
    public void readShortByIdTest() {
        //when
        RealEstateDto realEstate = realEstateService.readShortById(realEstate1.getId());

        //then
        assertThat(realEstate, notNullValue());
        assertThat(realEstate.getId(), is(realEstate1.getId()));
        assertThat(realEstate.getName(), is(realEstate1.getName()));
    }

    @Test
    public void readFullByIdTest() {
        //when
        RealEstateFullDto realEstate = realEstateService.readFullById(realEstate1.getId());

        //then
        assertThat(realEstate, notNullValue());
        assertThat(realEstate.getId(), is(realEstate1.getId()));
        assertThat(realEstate.getName(), is(realEstate1.getName()));
    }

    @Test
    public void readAllShortsTest() {
        //given
        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<RealEstateDto> realEstates = realEstateService.readAllShorts(new RealEstateFilter(), pageable);

        //then
        assertThat(realEstates, notNullValue());
        assertThat(realEstates.getContent().isEmpty(), is(false));
    }

    @Test
    public void readAllFullsTest() {
        //given
        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<RealEstateFullDto> realEstates = realEstateService.readAllFulls(new RealEstateFilter(), pageable);

        //then
        assertThat(realEstates, notNullValue());
        assertThat(realEstates.getContent().isEmpty(), is(false));
    }

    @Test
    public void updateTest() {
        RealEstate toUpdate = RealEstate.builder()
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
                .announcementType(AnnouncementType.LONG_RENT)
                .roomsCount((short) 1)
                .ceilingHeight(0)
                .documents("")
                .isPrivate(false)
                .called(false)
                .calledAt(Instant.now())
                .realtor(realtor1)
                .build();
        realEstateRepository.save(toUpdate);
        RealEstateAddDto newData = RealEstateAddDto.builder()
                .name("newName")
                .description("newDescription")
                .price(BigDecimal.TEN)
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
                .build();
        long countBefore = realEstateRepository.count();

        //when
        RealEstateFullDto updated = realEstateService.update(toUpdate.getId(), newData);

        //then
        long countAfter = realEstateRepository.count();
        assertThat(countAfter, is(countBefore));

        assertThat(updated, notNullValue());
        assertThat(updated.getName(), is(newData.getName()));
        assertThat(updated.getDescription(), is(newData.getDescription()));

        Optional<RealEstate> optional = realEstateRepository.findById(toUpdate.getId());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get().getName(), is(newData.getName()));
        assertThat(optional.get().getDescription(), is(newData.getDescription()));
    }

    @Test
    public void updateVerifiedTest() {
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
                .called(false)
                .calledAt(Instant.now())
                .realtor(realtor1)
                .build();
        realEstateRepository.save(realEstate);
        long countBefore = realEstateRepository.count();
        boolean verifiedToPut = true;

        //when
        Boolean verified = realEstateService.updateVerified(realEstate.getId(), verifiedToPut);

        //then
        long countAfter = realEstateRepository.count();
        assertThat(countAfter, is(countBefore));

        assertThat(verified, notNullValue());
        assertThat(verified, is(verifiedToPut));

        Optional<RealEstate> optional = realEstateRepository.findById(realEstate.getId());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get().isVerified(), is(verified));
    }

    @Test
    public void updateCalledTest() {
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
                .called(false)
                .calledAt(Instant.now())
                .realtor(realtor1)
                .build();
        realEstateRepository.save(realEstate);
        long countBefore = realEstateRepository.count();
        boolean calledToSet = true;

        //when
        Boolean called = realEstateService.updateCalled(realEstate.getId(), calledToSet);

        //then
        long countAfter = realEstateRepository.count();
        assertThat(countAfter, is(countBefore));

        assertThat(called, notNullValue());
        assertThat(called, is(calledToSet));

        Optional<RealEstate> optional = realEstateRepository.findById(realEstate.getId());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get().isCalled(), is(called));
    }

    @Test
    public void deleteTest() {
        //given
        long countBefore = realEstateRepository.count();

        //when
        realEstateService.delete(realEstate1.getId());

        //then
        long countAfter = realEstateRepository.count();
        assertThat(countAfter, is(countBefore - 1));

        Optional<RealEstate> optional = realEstateRepository.findById(realEstate1.getId());
        assertThat(optional.isPresent(), is(false));
    }


}
