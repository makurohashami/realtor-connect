package com.kotyk.realtorconnect.enumconverter.realestate;

import com.kotyk.realtorconnect.entity.realestate.enumeration.WindowsType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class WindowsTypeConverter implements AttributeConverter<WindowsType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(WindowsType attribute) {
        return attribute.getTypeId();
    }

    @Override
    public WindowsType convertToEntityAttribute(Integer dbData) {
        return WindowsType.getById(dbData);
    }

}
