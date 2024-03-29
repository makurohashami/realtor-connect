package com.kotyk.realtorconnect.enumconverter.realestate;

import com.kotyk.realtorconnect.entity.realestate.enumeration.BathroomType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BathroomTypeConverter implements AttributeConverter<BathroomType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BathroomType attribute) {
        return attribute.getTypeId();
    }

    @Override
    public BathroomType convertToEntityAttribute(Integer dbData) {
        return BathroomType.getById(dbData);
    }

}
