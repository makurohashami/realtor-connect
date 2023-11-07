package com.kotyk.realtorconnect.entity.realestate.embedded;

import com.kotyk.realtorconnect.entity.realestate.enums.BathroomType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bathroom {

    @Column(name = "bathroom_type_id")
    private BathroomType type;
    @Column(name = "bathrooms_count")
    private byte count;
    @Column(name = "is_combined")
    private boolean combined;

}
