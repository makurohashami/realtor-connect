package com.kotyk.realtorconnect.dto.realtor;

import com.kotyk.realtorconnect.dto.user.UserAddDto;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RealtorAddDto extends UserAddDto {

    @Size(min = 3, max = 50)
    private String agency;
    @Size(min = 3, max = 2048)
    private String agencySite;

}
