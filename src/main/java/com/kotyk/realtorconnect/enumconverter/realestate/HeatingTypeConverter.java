package com.kotyk.realtorconnect.enumconverter.realestate;

import com.kotyk.realtorconnect.entity.realestate.enums.HeatingType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class HeatingTypeConverter implements AttributeConverter<HeatingType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(HeatingType attribute) {
        return attribute.getTypeId();
    }

    @Override
    public HeatingType convertToEntityAttribute(Integer dbData) {
        return HeatingType.getById(dbData);
    }

}
