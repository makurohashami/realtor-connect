package com.kotyk.realtorconnect.dto.realtor;

import com.kotyk.realtorconnect.dto.realestate.RealEstateFullDto;
import com.kotyk.realtorconnect.entity.realtor.SubscriptionType;
import com.kotyk.realtorconnect.entity.user.Role;
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
public class RealtorFullDto {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private Role role;
    private Instant lastLogin;
    private Boolean blocked;
    private String agency;
    private String agencySite;
    private SubscriptionType subscriptionType;
    private Instant premiumExpiresAt;
    private List<ContactDto> contacts = new ArrayList<>();
    private List<RealEstateFullDto> realEstates = new ArrayList<>();

}
