package com.kotyk.realtorconnect.enumconverter.realestate;

import com.kotyk.realtorconnect.entity.realestate.enums.StateType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StateTypeConverter implements AttributeConverter<StateType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StateType attribute) {
        return attribute.getTypeId();
    }

    @Override
    public StateType convertToEntityAttribute(Integer dbData) {
        return StateType.getById(dbData);
    }

}
