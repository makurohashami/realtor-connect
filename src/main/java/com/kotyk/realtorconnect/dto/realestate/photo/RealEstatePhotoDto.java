package com.kotyk.realtorconnect.dto.realestate.photo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstatePhotoDto {

    private Long id;
    private String photo;
    private Long order;
    private boolean isPrivate;

}
