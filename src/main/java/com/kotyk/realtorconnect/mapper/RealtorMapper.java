package com.kotyk.realtorconnect.mapper;

import com.kotyk.realtorconnect.dto.realtor.RealtorAddDto;
import com.kotyk.realtorconnect.dto.realtor.RealtorDto;
import com.kotyk.realtorconnect.dto.realtor.RealtorFullDto;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {RealEstateMapper.class, UserMapper.class})
public interface RealtorMapper {

    default Realtor fromId(Long id) {
        if (id == null) return null;
        return Realtor.builder().id(id).build();
    }

    RealtorDto toDto(Realtor realtor);

    RealtorFullDto toFullDto(Realtor realtor);

    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    @Mapping(target = "role", constant = "REALTOR")
    @Mapping(target = "blocked", constant = "false")
    @Mapping(target = "created", expression = "java( java.time.Instant.now() )")
    @Mapping(target = "subscriptionType", constant = "FREE")
    @Mapping(target = "realEstatesCount", constant = "0")
    @Mapping(target = "publicRealEstatesCount", constant = "0")
    Realtor toEntity(RealtorAddDto dto);

    @Mapping(source = "password", target = "password", qualifiedByName = "encodePassword")
    Realtor update(@MappingTarget Realtor realtor, RealtorAddDto dto);
}
