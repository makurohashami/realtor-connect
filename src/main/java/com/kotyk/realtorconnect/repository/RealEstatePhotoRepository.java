package com.kotyk.realtorconnect.repository;

import com.kotyk.realtorconnect.entity.realestate.RealEstatePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealEstatePhotoRepository extends JpaRepository<RealEstatePhoto, Long> {

    List<RealEstatePhoto> findAllByRealEstateId(long realEstateId);

}
