package com.kotyk.realtorconnect.dto.realtor;

import com.kotyk.realtorconnect.entity.realtor.ContactType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {

    private Long id;
    private ContactType type;
    private String contact;

}
