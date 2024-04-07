package com.kotyk.realtorconnect.contact;

import com.kotyk.realtorconnect.dto.realtor.ContactDto;
import com.kotyk.realtorconnect.entity.realtor.Contact;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.entity.realtor.enumeration.ContactType;
import com.kotyk.realtorconnect.entity.realtor.enumeration.SubscriptionType;
import com.kotyk.realtorconnect.entity.user.Role;
import com.kotyk.realtorconnect.repository.ContactRepository;
import com.kotyk.realtorconnect.repository.RealtorRepository;
import com.kotyk.realtorconnect.service.contact.ContactService;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser("realtor")
public class ContactServiceITest {

    @Autowired
    ContactService contactService;
    @Autowired
    RealtorRepository realtorRepository;
    @Autowired
    ContactRepository contactRepository;

    Realtor realtor = Realtor.builder()
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

    Contact contact1 = Contact.builder()
            .type(ContactType.EMAIL)
            .contact("realtor@mail.com")
            .realtor(realtor)
            .build();

    Contact contact2 = Contact.builder()
            .type(ContactType.PHONE)
            .contact("phone@mail.com")
            .realtor(realtor)
            .build();

    @BeforeEach
    public void init(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();

        realtorRepository.save(realtor);
        contactRepository.saveAll(List.of(contact1, contact2));

        realtor.setContacts(Set.of(contact1, contact2));
    }

    @Test
    public void createTest() {
        //given
        long countBefore = contactRepository.count();
        ContactDto newContact = ContactDto.builder()
                .contact("newContact")
                .type(ContactType.EMAIL)
                .build();

        //when
        ContactDto saved = contactService.create(realtor.getId(), newContact);

        //then
        long countAfter = contactRepository.count();
        assertThat(countAfter, is(not(countBefore)));
        assertThat(countAfter, is(countBefore + 1));

        assertThat(saved, notNullValue());
        assertThat(saved.getContact(), is(newContact.getContact()));
        assertThat(saved.getType(), is(newContact.getType()));

        Optional<Contact> optional = contactRepository.findById(saved.getId());
        assertThat(optional, notNullValue());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get().getId(), is(saved.getId()));
    }

    @Test
    public void readByIdTest() {
        //when
        ContactDto contactDto = contactService.readById(contact1.getId());

        //then
        assertThat(contactDto, notNullValue());
        assertThat(contactDto.getId(), is(contact1.getId()));
        assertThat(contactDto.getContact(), is(contact1.getContact()));
        assertThat(contactDto.getType(), is(contact1.getType()));
    }

    @Test
    public void readByIdWithBadIdTest() {
        //given
        long badId = -1L;

        //when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> contactService.readById(badId));

        //then
        assertThat(exception, notNullValue());
        assertThat(exception, instanceOf(ResourceNotFoundException.class));
        assertThat(exception.getMessage(), notNullValue());
        assertThat(exception.getMessage(), is(String.format(ContactService.NOT_FOUND_BY_ID_MSG, badId)));
    }

    @Test
    public void readAllTest() {
        //when
        List<ContactDto> contacts = contactService.readAll(realtor.getId());

        //then
        assertThat(contacts, notNullValue());
        assertThat(contacts.size(), is(realtor.getContacts().size()));
    }

    @Test
    public void readAllWithNotExistingRealtorTest() {
        //given
        long badId = -1L;

        //when
        List<ContactDto> contacts = contactService.readAll(badId);

        //then
        assertThat(contacts, notNullValue());
        assertThat(contacts.size(), is(0));
    }

    @Test
    public void updateTest() {
        //given
        long countBefore = contactRepository.count();
        ContactDto contactToUpdate = ContactDto.builder()
                .contact("updatedContact")
                .type(ContactType.TELEGRAM)
                .build();

        //when
        ContactDto updated = contactService.update(contact2.getId(), contactToUpdate);

        //then
        long countAfter = contactRepository.count();
        assertThat(countAfter, is(countBefore));

        assertThat(updated, notNullValue());
        assertThat(updated.getContact(), is(contactToUpdate.getContact()));
        assertThat(updated.getType(), is(contactToUpdate.getType()));

        Optional<Contact> optional = contactRepository.findById(updated.getId());
        assertThat(optional, notNullValue());
        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get().getId(), is(updated.getId()));
        assertThat(optional.get().getContact(), is(updated.getContact()));
        assertThat(optional.get().getType(), is(updated.getType()));
    }

    @Test
    public void deleteTest() {
        //given
        long countBefore = contactRepository.count();

        //when
        contactService.delete(contact1.getId());

        //then
        long countAfter = contactRepository.count();
        assertThat(countAfter, is(not(countBefore)));
        assertThat(countAfter, is(countBefore - 1));
    }

    @Test
    public void deleteWithWrongIdAndNothingChangedTest() {
        //given
        long countBefore = contactRepository.count();

        //when
        contactService.delete(-1);

        //then
        assertThat(contactRepository.count(), is(countBefore));
    }

}
