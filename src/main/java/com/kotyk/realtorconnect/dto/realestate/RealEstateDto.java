package com.kotyk.realtorconnect.dto.realestate;

import com.kotyk.realtorconnect.dto.realestate.photo.RealEstatePhotoDto;
import com.kotyk.realtorconnect.entity.realestate.embedded.Area;
import com.kotyk.realtorconnect.entity.realestate.embedded.Bathroom;
import com.kotyk.realtorconnect.entity.realestate.embedded.Loggia;
import com.kotyk.realtorconnect.entity.realestate.enumeration.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateDto {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean verified;
    private LocationDto location;
    private Loggia loggia;
    private Bathroom bathroom;
    private Area area;
    private short floor;
    private short floorsInBuilding;
    private BuildingType buildingType;
    private HeatingType heatingType;
    private WindowsType windowsType;
    private HotWaterType hotWaterType;
    private StateType stateType;
    private AnnouncementType announcementType;
    private short roomsCount;
    private double ceilingHeight;
    private Instant calledAt;
    private List<RealEstatePhotoDto> photos = new ArrayList<>();
    private boolean isPrivate;
    private long realtorId;

}
