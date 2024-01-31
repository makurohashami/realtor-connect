package com.kotyk.realtorconnect.mapper;

import com.kotyk.realtorconnect.dto.realestate.RealEstateAddDto;
import com.kotyk.realtorconnect.dto.realestate.RealEstateDto;
import com.kotyk.realtorconnect.dto.realestate.RealEstateFullDto;
import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RealEstateMapper {

    @Named("fromId")
    default Realtor fromId(Long id) {
        if (id == null) return null;
        return Realtor.builder().id(id).build();
    }

    @Named("countFavorites")
    default int countFavorites(Set<User> favorites) {
        return favorites.size();
    }

    @Mapping(source = "realtor.id", target = "realtorId")
    @Mapping(source = "favorites", target = "countFavorites", qualifiedByName = "countFavorites")
    RealEstateDto toDto(RealEstate realEstate);

    @Mapping(source = "realtor.id", target = "realtorId")
    @Mapping(source = "favorites", target = "countFavorites", qualifiedByName = "countFavorites")
    RealEstateFullDto toFullDto(RealEstate realEstate);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "realtorId", target = "realtor", qualifiedByName = "fromId")
    @Mapping(target = "verified", constant = "false")
    @Mapping(target = "countPublicPhotos", constant = "0")
    @Mapping(target = "countPhotos", constant = "0")
    @Mapping(target = "called", constant = "true")
    @Mapping(target = "calledAt", expression = "java( java.time.Instant.now() )")
    @Mapping(target = "createdAt", expression = "java( java.time.Instant.now() )")
    RealEstate toEntity(RealEstateAddDto dto, long realtorId);

    RealEstate update(@MappingTarget RealEstate toUpdate, RealEstateAddDto dto);
}
