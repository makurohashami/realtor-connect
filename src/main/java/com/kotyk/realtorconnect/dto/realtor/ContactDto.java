package com.kotyk.realtorconnect.dto.realtor;

import com.kotyk.realtorconnect.entity.realtor.ContactType;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {

    private Long id;
    private ContactType type;
    private String contact;

}
