package com.kotyk.realtorconnect.entity.realtor;

import com.kotyk.realtorconnect.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "realtors_info")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Realtor extends User {

    private String agency;
    private String agencySite;
    private SubscriptionType subscriptionType;
    private Integer announcementCount;
    private Integer publicAnnouncementCount;

}
