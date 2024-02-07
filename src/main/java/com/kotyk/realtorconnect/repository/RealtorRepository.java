package com.kotyk.realtorconnect.repository;

import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.entity.realtor.SubscriptionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RealtorRepository extends JpaRepository<Realtor, Long> {

    Page<Realtor> findAll(Specification<Realtor> spec, Pageable pageable);

    List<Realtor> findAllByPremiumExpiresAtBeforeAndSubscriptionType(Instant instant, SubscriptionType type);

    @Modifying
    @Query("UPDATE Realtor r SET r.publicRealEstatesCount = :publicRealEstatesCount WHERE r.id = :id")
    void setRealEstateCountsByRealtorId(long id, int publicRealEstatesCount);

}
