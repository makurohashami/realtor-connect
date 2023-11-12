package com.kotyk.realtorconnect.entity.realestate.embedded;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @NotNull
    @Size(min = 3, max = 255)
    private String city;
    @NotNull
    @Size(min = 3, max = 255)
    private String district;
    @NotNull
    @Size(min = 3, max = 255)
    private String residentialArea;
    @NotNull
    @Size(min = 3, max = 255)
    private String street;
    @Size(min = 3, max = 255)
    private String housingEstate;
    @NotNull
    @Min(1)
    private int houseNumber;
    private String block;
    @NotNull
    @Min(1)
    private int apartmentNumber;
    @Size(min = 3, max = 255)
    private String landmark;

}
