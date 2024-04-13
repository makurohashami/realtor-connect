package com.kotyk.realtorconnect.dto.realestate;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    private String city;
    private String district;
    private String residentialArea;
    private String street;
    private String housingEstate;
    private String landmark;

}
