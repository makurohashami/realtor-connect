package com.kotyk.realtorconnect.mapper;

import com.kotyk.realtorconnect.dto.realtor.ContactDto;
import com.kotyk.realtorconnect.entity.realtor.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RealtorMapper.class})
public interface ContactMapper {

    ContactDto toDto(Contact contact);

    List<ContactDto> toListDto(List<Contact> contacts);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "realtorId", target = "realtor")
    Contact toEntity(ContactDto dto, long realtorId);

    Contact update(@MappingTarget Contact contact, ContactDto dto);

}
