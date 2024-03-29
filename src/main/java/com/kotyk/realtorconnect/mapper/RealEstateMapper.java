package com.kotyk.realtorconnect.mapper;

import com.kotyk.realtorconnect.dto.realestate.RealEstateAddDto;
import com.kotyk.realtorconnect.dto.realestate.RealEstateDto;
import com.kotyk.realtorconnect.dto.realestate.RealEstateFullDto;
import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RealEstateMapper {

    @Named("fromId")
    default Realtor fromId(Long id) {
        if (id == null) return null;
        return Realtor.builder().id(id).build();
    }

    @Mapping(source = "realtor.id", target = "realtorId")
    RealEstateDto toDto(RealEstate realEstate);

    @Mapping(source = "realtor.id", target = "realtorId")
    RealEstateFullDto toFullDto(RealEstate realEstate);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "realtorId", target = "realtor", qualifiedByName = "fromId")
    @Mapping(target = "verified", constant = "false")
    @Mapping(target = "called", constant = "true")
    @Mapping(target = "calledAt", expression = "java( java.time.Instant.now() )")
    @Mapping(target = "createdAt", expression = "java( java.time.Instant.now() )")
    RealEstate toEntity(RealEstateAddDto dto, long realtorId);

    RealEstate update(@MappingTarget RealEstate toUpdate, RealEstateAddDto dto);
}
