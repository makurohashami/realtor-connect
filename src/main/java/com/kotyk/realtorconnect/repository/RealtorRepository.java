package com.kotyk.realtorconnect.repository;

import com.kotyk.realtorconnect.entity.realtor.Realtor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealtorRepository extends JpaRepository<Realtor, Long> {
}
