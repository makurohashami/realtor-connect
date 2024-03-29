package com.kotyk.realtorconnect.entity.realestate;

import com.kotyk.realtorconnect.entity.realestate.listener.RealEstatePhotoListener;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "real_estates_photos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(RealEstatePhotoListener.class)
public class RealEstatePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String photo;
    @Column(name = "photo_id")
    private String photoId;
    @Column(name = "order_num")
    private Long order;
    private boolean isPrivate;
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "real_estate_id", nullable = false)
    private RealEstate realEstate;

}
