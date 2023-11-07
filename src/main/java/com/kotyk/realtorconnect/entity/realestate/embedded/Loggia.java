package com.kotyk.realtorconnect.entity.realestate.embedded;

import com.kotyk.realtorconnect.entity.realestate.enums.LoggiaType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loggia {

    @Column(name = "loggia_type_id")
    private LoggiaType type;
    @Column(name = "loggias_count")
    private byte count;
    @Column(name = "is_glassed")
    private boolean glassed;

}
