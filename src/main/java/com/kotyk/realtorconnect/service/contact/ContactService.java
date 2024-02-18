package com.kotyk.realtorconnect.service.contact;

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
        return contactMapper.toDto(contactRepository.save(
                contactMapper.toEntity(contactDto, realtorId)));
    }

    @Transactional(readOnly = true)
    public ContactDto readById(long contactId) {
        return contactMapper.toDto(contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, contactId))));
    }

    @Transactional(readOnly = true)
    public List<ContactDto> readAll(long realtorId) {
        return contactMapper.toListDto(contactRepository.findAllByRealtorId(realtorId));
    }

    @Transactional
    public ContactDto update(long contactId, ContactDto contactDto) {
        Contact toUpdate = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, contactId)));
        return contactMapper.toDto(contactMapper.update(toUpdate, contactDto));
    }

    @Transactional
    public void delete(long contactId) {
        contactRepository.deleteById(contactId);
    }


}
