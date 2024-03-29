package com.kotyk.realtorconnect.dto.realtor;

import com.kotyk.realtorconnect.dto.user.UserFullDto;
import com.kotyk.realtorconnect.entity.realtor.enumeration.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RealtorFullDto extends UserFullDto {

    private String agency;
    private String agencySite;
    private SubscriptionType subscriptionType;
    private Instant premiumExpiresAt;
    private List<ContactDto> contacts = new ArrayList<>();

}
