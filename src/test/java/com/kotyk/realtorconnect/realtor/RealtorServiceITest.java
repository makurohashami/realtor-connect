package com.kotyk.realtorconnect.realtor;

import com.kotyk.realtorconnect.dto.realtor.RealtorAddDto;
import com.kotyk.realtorconnect.dto.realtor.RealtorDto;
import com.kotyk.realtorconnect.dto.realtor.RealtorFilter;
import com.kotyk.realtorconnect.dto.realtor.RealtorFullDto;
import com.kotyk.realtorconnect.entity.realtor.Contact;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.entity.realtor.enumeration.ContactType;
import com.kotyk.realtorconnect.entity.realtor.enumeration.SubscriptionType;
import com.kotyk.realtorconnect.entity.user.Role;
import com.kotyk.realtorconnect.repository.ContactRepository;
import com.kotyk.realtorconnect.repository.RealtorRepository;
import com.kotyk.realtorconnect.service.realtor.RealtorService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser
public class RealtorServiceITest {

    @Autowired
    RealtorService realtorService;
    @Autowired
    RealtorRepository realtorRepository;
    @Autowired
    ContactRepository contactRepository;

    Realtor realtor1 = Realtor.builder()
            .name("realtor1")
            .username("realtor1")
            .password("realtor1")
            .email("realtor1")
            .blocked(false)
            .emailVerified(true)
            .role(Role.REALTOR)
            .subscriptionType(SubscriptionType.FREE)
            .publicRealEstatesCount(0)
            .agency("realtor1")
            .agencySite("realtor1")
            .build();

    Contact contact1 = Contact.builder()
            .realtor(realtor1)
            .type(ContactType.EMAIL)
            .contact("contact1")
            .build();

    @BeforeEach
    public void init(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();

        realtorRepository.save(realtor1);
        contactRepository.save(contact1);

        Set<Contact> contacts = new HashSet<>(Set.of(contact1));
        realtor1.setContacts(contacts);
        realtorRepository.save(realtor1);
    }

    @Test
    public void createTest() {
        //given
        RealtorAddDto realtorToAdd = RealtorAddDto.builder()
                .name("realtorToAdd")
                .username("realtorToAdd")
                .password("realtorToAdd")
                .email("realtorToAdd")
                .agency("realtorToAdd")
                .agencySite("realtorToAdd")
                .build();
        long countBefore = realtorRepository.count();

        //when
        RealtorFullDto added = realtorService.create(realtorToAdd);

        //then
        long countAfter = realtorRepository.count();
        assertThat(countAfter, is(countBefore + 1));

        assertThat(added, notNullValue());
        assertThat(added.getId(), notNullValue());
        assertThat(added.getName(), is(realtorToAdd.getName()));
        assertThat(added.getAgency(), is(realtorToAdd.getAgency()));
        assertThat(added.getPhone(), is(realtorToAdd.getPhone()));

        Optional<Realtor> optional = realtorRepository.findById(added.getId());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get().getId(), is(added.getId()));
        assertThat(optional.get().getAgency(), is(added.getAgency()));
    }

    @Test
    public void readFullByIdTest() {
        //when
        RealtorFullDto realtor = realtorService.readFullById(realtor1.getId());

        //then
        assertThat(realtor, notNullValue());
        assertThat(realtor.getId(), notNullValue());
        assertThat(realtor.getId(), is(realtor1.getId()));
        assertThat(realtor.getAgency(), is(realtor.getAgency()));
        assertThat(realtor.getContacts(), notNullValue());
        assertThat(realtor.getContacts().size(), is(1));
        assertThat(realtor.getContacts().get(0).getId(), is(contact1.getId()));
    }

    @Test
    @WithMockUser(authorities = "SEE_REALTORS_CONTACTS")
    public void readShortByIdWithAuthTest() {
        //when
        RealtorDto realtor = realtorService.readShortById(realtor1.getId());

        //then
        assertThat(realtor, notNullValue());
        assertThat(realtor.getId(), notNullValue());
        assertThat(realtor.getId(), is(realtor1.getId()));
        assertThat(realtor.getAgency(), is(realtor.getAgency()));
        assertThat(realtor.getContacts(), notNullValue());
        assertThat(realtor.getContacts().size(), is(1));
        assertThat(realtor.getContacts().get(0).getId(), is(contact1.getId()));
    }

    @Test
    @WithAnonymousUser
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void readShortByIdWithoutAuthTest() {
        //when
        RealtorDto realtor = realtorService.readShortById(realtor1.getId());

        //then
        assertThat(realtor, notNullValue());
        assertThat(realtor.getId(), notNullValue());
        assertThat(realtor.getId(), is(realtor1.getId()));
        assertThat(realtor.getAgency(), is(realtor.getAgency()));
        assertThat(realtor.getContacts().size(), is(0));
    }

    @Test
    @WithMockUser(authorities = "SEE_REALTORS_CONTACTS")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void getAllShortsWithAuthTest() {
        //given
        Pageable pageable = PageRequest.of(0, 5);

        //when
        Page<RealtorDto> realtors = realtorService.getAllShorts(new RealtorFilter(), pageable);

        //then
        assertThat(realtors, notNullValue());
        assertThat(realtors.getContent(), notNullValue());
        assertThat(realtors.getNumberOfElements() > 0, is(true));
        assertThat(realtors.getContent().get(0).getId(), is(realtor1.getId()));
        assertThat(realtors.getContent().get(0).getContacts(), notNullValue());
        assertThat(realtors.getContent().get(0).getContacts().isEmpty(), is(false));
        assertThat(realtors.getContent().get(0).getContacts().get(0).getId(), is(contact1.getId()));
    }

    @Test
    @WithAnonymousUser
    public void getAllShortsWithoutAuthTest() {
        //given
        Pageable pageable = PageRequest.of(0, 5);

        //when
        Page<RealtorDto> realtors = realtorService.getAllShorts(new RealtorFilter(), pageable);

        //then
        assertThat(realtors, notNullValue());
        assertThat(realtors.getContent(), notNullValue());
        assertThat(realtors.getNumberOfElements() > 0, is(true));
        assertThat(realtors.getContent().get(0).getId(), is(realtor1.getId()));
        assertThat(realtors.getContent().get(0).getContacts(), notNullValue());
        assertThat(realtors.getContent().get(0).getContacts().isEmpty(), is(true));
    }

    @Test
    public void updateTest() {
        //given
        Realtor realtorToUpdate = Realtor.builder()
                .name("realtorToUpdate")
                .username("realtorToUpdate")
                .password("realtorToUpdate")
                .email("realtorToUpdate")
                .blocked(false)
                .emailVerified(true)
                .role(Role.REALTOR)
                .subscriptionType(SubscriptionType.FREE)
                .publicRealEstatesCount(0)
                .agency("realtorToUpdate")
                .agencySite("realtorToUpdate")
                .build();
        realtorRepository.save(realtorToUpdate);
        RealtorAddDto newRealtorInfo = RealtorAddDto.builder()
                .name("newInfo")
                .username("newInfo")
                .password("newInfo")
                .email("newInfo")
                .build();
        long countBefore = realtorRepository.count();

        //when
        RealtorFullDto updated = realtorService.update(realtorToUpdate.getId(), newRealtorInfo);

        //then
        long countAfter = realtorRepository.count();
        assertThat(countAfter, is(countBefore));

        assertThat(updated, notNullValue());
        assertThat(updated.getId(), notNullValue());
        assertThat(updated.getName(), is(newRealtorInfo.getName()));

        Optional<Realtor> optional = realtorRepository.findById(updated.getId());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get().getName(), is(updated.getName()));
    }

    @Test
    public void deleteTest() {
        //given
        long countBefore = realtorRepository.count();

        //when
        realtorService.delete(realtor1.getId());

        //then
        long countAfter = realtorRepository.count();
        assertThat(countAfter, is(countBefore - 1));
        Optional<Realtor> optional = realtorRepository.findById(realtor1.getId());
        assertThat(optional.isPresent(), is(false));
    }

    @Test
    public void givePremiumToRealtorTest() {
        //given
        Realtor realtor = Realtor.builder()
                .name("realtor")
                .username("realtor")
                .password("realtor")
                .email("realtor")
                .blocked(false)
                .emailVerified(true)
                .role(Role.REALTOR)
                .subscriptionType(SubscriptionType.FREE)
                .publicRealEstatesCount(0)
                .agency("realtor")
                .agencySite("realtor")
                .build();
        realtorRepository.save(realtor);
        short duration = 3;

        //when
        Instant expireTime = realtorService.givePremiumToRealtor(realtor.getId(), duration);

        //then
        assertThat(expireTime, notNullValue());
        assertThat(expireTime.isAfter(Instant.now()), is(true));
        int currentMouth = ZonedDateTime.now().getMonthValue();
        int expireMonth = ZonedDateTime.ofInstant(expireTime, ZoneId.systemDefault()).getMonthValue();
        assertThat(currentMouth, is(expireMonth - duration));
    }

}
