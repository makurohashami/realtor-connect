package com.kotyk.realtorconnect.dto.realestate;

import com.kotyk.realtorconnect.entity.realestate.embedded.*;
import com.kotyk.realtorconnect.entity.realestate.enums.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateAddDto {

    @NotNull
    @Size(min = 3, max = 255)
    private String name;
    @NotNull
    @Size(min = 40, max = 512)
    private String description;
    @NotNull
    @Min(0)
    @Max(999000000)
    private BigDecimal price;
    @Valid
    private Owner owner;
    @Valid
    private Location location;
    @Valid
    private Loggia loggia;
    @Valid
    private Bathroom bathroom;
    @Valid
    private Area area;
    @NotNull
    @Min(1)
    @Max(127)
    private short floor;
    @NotNull
    @Min(1)
    @Max(127)
    private short floorsInBuilding;
    @NotNull
    private BuildingType buildingType;
    @NotNull
    private HeatingType heatingType;
    @NotNull
    private WindowsType windowsType;
    @NotNull
    private HotWaterType hotWaterType;
    @NotNull
    private StateType stateType;
    @NotNull
    private AnnouncementType announcementType;
    @NotNull
    @Min(1)
    @Max(127)
    private short roomsCount;
    @Min(0)
    private double ceilingHeight;
    @Size(max = 512)
    private String documents;
    @NotNull
    private boolean isPrivate;

}
