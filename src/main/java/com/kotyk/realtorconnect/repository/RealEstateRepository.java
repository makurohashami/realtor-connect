package com.kotyk.realtorconnect.repository;

import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RealEstateRepository extends JpaRepository<RealEstate, Long> {

    Page<RealEstate> findAll(Specification<RealEstate> spec, Pageable pageable);

    List<RealEstate> findAllByCalledAtBeforeAndCalledTrue(Instant instant);

}
