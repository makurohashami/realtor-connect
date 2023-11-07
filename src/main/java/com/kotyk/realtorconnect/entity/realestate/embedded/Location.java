package com.kotyk.realtorconnect.entity.realestate.embedded;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private String city;
    private String district;
    private String residentialArea;
    private String street;
    private String housingEstate;
    private int houseNumber;
    private String block;
    private int apartmentNumber;
    private String landmark;

}
