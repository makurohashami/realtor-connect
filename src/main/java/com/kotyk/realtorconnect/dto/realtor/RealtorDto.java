package com.kotyk.realtorconnect.dto.realtor;

import com.kotyk.realtorconnect.dto.user.UserDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RealtorDto extends UserDto {

    private String agency;
    private String agencySite;
    private List<ContactDto> contacts = new ArrayList<>();

}
