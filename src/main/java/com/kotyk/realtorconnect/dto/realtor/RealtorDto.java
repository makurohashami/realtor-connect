package com.kotyk.realtorconnect.dto.realtor;

import com.kotyk.realtorconnect.dto.realestate.RealEstateDto;
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
public class RealtorDto {

    private Long id;
    private String name;
    private String avatar;
    private String agency;
    private String agencySite;
    private List<ContactDto> contacts = new ArrayList<>();
    private List<RealEstateDto> realEstates = new ArrayList<>();

}
