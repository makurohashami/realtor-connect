package com.kotyk.realtorconnect.enumconverter.realestate;

import com.kotyk.realtorconnect.entity.realestate.enums.LoggiaType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LoggiaTypeConverter implements AttributeConverter<LoggiaType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(LoggiaType attribute) {
        return attribute.getTypeId();
    }

    @Override
    public LoggiaType convertToEntityAttribute(Integer dbData) {
        return LoggiaType.getById(dbData);
    }

}
