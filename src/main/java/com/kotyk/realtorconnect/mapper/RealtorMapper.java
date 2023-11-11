package com.kotyk.realtorconnect.mapper;

import com.kotyk.realtorconnect.dto.realtor.RealtorDto;
import com.kotyk.realtorconnect.dto.realtor.RealtorFullDto;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = RealEstateMapper.class)
public interface RealtorMapper {

    RealtorDto toDto(Realtor realtor);

    RealtorFullDto toFullDto(Realtor realtor);

}
