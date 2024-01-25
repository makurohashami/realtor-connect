package com.kotyk.realtorconnect.entity.realestate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "real_estates_photos")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RealEstatePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String photo;
    private boolean isPrivate;
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "real_estate_id", nullable = false)
    private RealEstate realEstate;

}
