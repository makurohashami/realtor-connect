package com.kotyk.realtorconnect.dto.realestate;

import com.kotyk.realtorconnect.entity.realestate.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateFilter {

    private Long realtorId;
    private String name;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String city;
    private String district;
    private String residentialArea;
    private String street;
    private LoggiaType loggiaType;
    private Short loggiasCount;
    private Boolean isLoggiaGlassed;
    private BathroomType bathroomType;
    private Short bathroomsCount;
    private Boolean isBathroomCombined;
    private Double minTotalArea;
    private Double maxTotalArea;
    private Double minLivingArea;
    private Double maxLivingArea;
    private Double minKitchenArea;
    private Double maxKitchenArea;
    private Short minFloor;
    private Short maxFloor;
    private BuildingType buildingType;
    private HeatingType heatingType;
    private WindowsType windowsType;
    private HotWaterType hotWaterType;
    private StateType stateType;
    private AnnouncementType announcementType;
    private Short roomsCount;

}
