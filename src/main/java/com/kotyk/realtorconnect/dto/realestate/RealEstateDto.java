package com.kotyk.realtorconnect.dto.realestate;

import com.kotyk.realtorconnect.entity.realestate.embedded.Area;
import com.kotyk.realtorconnect.entity.realestate.embedded.Bathroom;
import com.kotyk.realtorconnect.entity.realestate.embedded.Loggia;
import com.kotyk.realtorconnect.entity.realestate.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
    private byte floor;
    private byte floorsInBuilding;
    private BuildingType buildingType;
    private HeatingType heatingType;
    private WindowsType windowsType;
    private HotWaterType hotWaterType;
    private StateType stateType;
    private AnnouncementType announcementType;
    private byte roomsCount;
    private double ceilingHeight;
    private Instant called_at;
    private List<RealEstatePhotoDto> photos = new ArrayList<>();
    private long realtorId;
    private int countFavorites;

}
