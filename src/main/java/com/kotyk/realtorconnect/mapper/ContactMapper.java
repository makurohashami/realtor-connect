package com.kotyk.realtorconnect.mapper;

import com.kotyk.realtorconnect.dto.realtor.ContactDto;
import com.kotyk.realtorconnect.entity.realtor.Contact;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    @Named("fromId")
    default Realtor fromId(Long id) {
        if (id == null) return null;
        return Realtor.builder().id(id).build();
    }

    ContactDto toDto(Contact contact);

    List<ContactDto> toListDto(List<Contact> contacts);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "realtorId", target = "realtor", qualifiedByName = "fromId")
    Contact toEntity(ContactDto dto, long realtorId);

    @Mapping(target = "id", ignore = true)
    Contact update(@MappingTarget Contact contact, ContactDto dto);

}
