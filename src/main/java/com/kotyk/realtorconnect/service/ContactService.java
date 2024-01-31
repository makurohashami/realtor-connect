package com.kotyk.realtorconnect.service;

import com.kotyk.realtorconnect.dto.realtor.ContactDto;
import com.kotyk.realtorconnect.entity.realtor.Contact;
import com.kotyk.realtorconnect.mapper.ContactMapper;
import com.kotyk.realtorconnect.repository.ContactRepository;
import com.kotyk.realtorconnect.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {

    private static final String NOT_FOUND_BY_ID_MSG = "Contact with id '%d' not found";

    private final ContactMapper contactMapper;
    private final ContactRepository contactRepository;

    @Transactional
    public ContactDto create(long realtorId, ContactDto contactDto) {
        log.debug("create() - start. realtorId = {}, contact = {}", realtorId, contactDto);
        ContactDto contact = contactMapper.toDto(contactRepository.save(
                contactMapper.toEntity(contactDto, realtorId)));
        log.debug("create() - end. contact = {}", contact);
        return contact;
    }

    @Transactional(readOnly = true)
    public ContactDto readById(long contactId) {
        log.debug("readById() - start. contactId = {}", contactId);
        ContactDto contact = contactMapper.toDto(contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, contactId))));
        log.debug("readById() - end. contact = {}", contact);
        return contact;
    }

    @Transactional(readOnly = true)
    public List<ContactDto> readAll(long realtorId) {
        log.debug("readAll() - start. realtorId = {}", realtorId);
        List<ContactDto> contacts = contactMapper.toListDto(contactRepository.findAllByRealtorId(realtorId));
        log.debug("readAll() - end. contacts count = {}", contacts.size());
        return contacts;
    }

    @Transactional
    public ContactDto update(long contactId, ContactDto contactDto) {
        log.debug("update() - start. contactId = {}, contact = {}", contactId, contactDto);
        Contact toUpdate = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, contactId)));
        ContactDto contact = contactMapper.toDto(contactMapper.update(toUpdate, contactDto));
        log.debug("update() - end. updated = {}", contact);
        return contact;
    }

    @Transactional
    public void delete(long contactId) {
        log.debug("delete() - start. contactId = {}", contactId);
        contactRepository.deleteById(contactId);
        log.debug("delete() - end. deleted");
    }


}
