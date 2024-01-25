package com.kotyk.realtorconnect.entity.realtor;

import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "realtors_info")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Realtor extends User {

    private String agency;
    private String agencySite;
    @OneToMany(mappedBy = "realtor", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @OrderBy("type asc")
    private Set<Contact> contacts = new HashSet<>();
    @OneToMany(mappedBy = "realtor", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @OrderBy("id asc")
    @EqualsAndHashCode.Exclude
    private Set<RealEstate> realEstates = new HashSet<>();
    private SubscriptionType subscriptionType;
    private int realEstatesCount;
    private int publicRealEstatesCount;

}
