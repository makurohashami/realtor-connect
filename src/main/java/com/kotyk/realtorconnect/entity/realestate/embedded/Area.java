package com.kotyk.realtorconnect.entity.realestate.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Area {

    @NotNull
    @Min(0)
    @Column(name = "total_area")
    private double total;
    @NotNull
    @Min(0)
    @Column(name = "living_area")
    private double living;
    @NotNull
    @Min(0)
    @Column(name = "kitchen_area")
    private double kitchen;

}
