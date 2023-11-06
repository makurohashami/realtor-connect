package com.kotyk.realtorconnect.enumconverter;

import com.kotyk.realtorconnect.entity.realtor.ContactType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ContactTypeConverter implements AttributeConverter<ContactType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ContactType attribute) {
        return attribute.getTypeId();
    }

    @Override
    public ContactType convertToEntityAttribute(Integer dbData) {
        return ContactType.getById(dbData);
    }

}
