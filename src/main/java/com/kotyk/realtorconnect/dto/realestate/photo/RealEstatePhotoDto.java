package com.kotyk.realtorconnect.dto.realestate.photo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RealEstatePhotoDto {

    private Long id;
    private String photo;
    private Long order;
    private boolean isPrivate;

}
