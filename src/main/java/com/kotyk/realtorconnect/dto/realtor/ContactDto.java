package com.kotyk.realtorconnect.dto.realtor;

import com.kotyk.realtorconnect.entity.realtor.enumeration.ContactType;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {

    private Long id;
    private ContactType type;
    private String contact;

}
