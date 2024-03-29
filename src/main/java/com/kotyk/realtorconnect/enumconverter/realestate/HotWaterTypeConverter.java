package com.kotyk.realtorconnect.enumconverter.realestate;

import com.kotyk.realtorconnect.entity.realestate.enumeration.HotWaterType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class HotWaterTypeConverter implements AttributeConverter<HotWaterType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(HotWaterType attribute) {
        return attribute.getTypeId();
    }

    @Override
    public HotWaterType convertToEntityAttribute(Integer dbData) {
        return HotWaterType.getById(dbData);
    }

}
