package com.kotyk.realtorconnect.entity.realestate;

import com.kotyk.realtorconnect.entity.realestate.embedded.*;
import com.kotyk.realtorconnect.entity.realestate.enumeration.*;
import com.kotyk.realtorconnect.entity.realestate.listener.RealEstateListener;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "real_estates")
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners({RealEstateListener.class, AuditingEntityListener.class})
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean verified;
    @OneToMany(mappedBy = "realEstate", cascade = CascadeType.REMOVE)
    @OrderBy("id asc")
    private Set<RealEstatePhoto> photos = new HashSet<>();
    @Embedded
    private Owner owner;
    @Embedded
    private Location location;
    @Embedded
    private Loggia loggia;
    @Embedded
    private Bathroom bathroom;
    @Embedded
    private Area area;
    private short floor;
    private short floorsInBuilding;
    @Column(name = "building_type_id")
    private BuildingType buildingType;
    @Column(name = "heating_type_id")
    private HeatingType heatingType;
    @Column(name = "windows_type_id")
    private WindowsType windowsType;
    @Column(name = "hot_water_type_id")
    private HotWaterType hotWaterType;
    @Column(name = "state_type_id")
    private StateType stateType;
    @Column(name = "announcement_type_id")
    private AnnouncementType announcementType;
    private short roomsCount;
    private double ceilingHeight;
    private String documents;
    private boolean isPrivate;
    @Column(name = "is_called")
    private boolean called;
    private Instant calledAt;
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "realtor_id", nullable = false)
    private Realtor realtor;
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

}
