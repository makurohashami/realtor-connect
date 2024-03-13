package com.kotyk.realtorconnect.enumconverter.realestate;

import com.kotyk.realtorconnect.entity.realestate.enumeration.BuildingType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BuildingTypeConverter implements AttributeConverter<BuildingType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BuildingType attribute) {
        return attribute.getTypeId();
    }

    @Override
    public BuildingType convertToEntityAttribute(Integer dbData) {
        return BuildingType.getById(dbData);
    }

}
