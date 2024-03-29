package com.kotyk.realtorconnect.mapper;

import com.kotyk.realtorconnect.dto.realestate.photo.RealEstatePhotoDto;
import com.kotyk.realtorconnect.dto.realestate.photo.RealEstatePhotoUpdateDto;
import com.kotyk.realtorconnect.entity.realestate.RealEstatePhoto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RealEstatePhotoMapper {

    RealEstatePhotoDto toDto(RealEstatePhoto photo);

    List<RealEstatePhotoDto> toListDto(List<RealEstatePhoto> photos);

    RealEstatePhoto update(@MappingTarget RealEstatePhoto toUpdate, RealEstatePhotoUpdateDto dto);

}
