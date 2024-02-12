package com.kotyk.realtorconnect.dto.realtor;

import com.kotyk.realtorconnect.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RealtorDto extends UserDto {

    private String agency;
    private String agencySite;
    private List<ContactDto> contacts = new ArrayList<>();

}
