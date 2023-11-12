package com.kotyk.realtorconnect.entity.realestate.embedded;

import com.kotyk.realtorconnect.entity.realestate.enums.BathroomType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bathroom {

    @NotNull
    @Column(name = "bathroom_type_id")
    private BathroomType type;
    @NotNull
    @Min(1)
    @Max(127)
    @Column(name = "bathrooms_count")
    private byte count;
    @NotNull
    @Column(name = "is_bathroom_combined")
    private boolean combined;

}
