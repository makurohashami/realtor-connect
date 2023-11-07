package com.kotyk.realtorconnect.entity.realestate.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Area {

    @Column(name = "total_area")
    private double total;
    @Column(name = "living_area")
    private double living;
    @Column(name = "kitchen_area")
    private double kitchen;

}
