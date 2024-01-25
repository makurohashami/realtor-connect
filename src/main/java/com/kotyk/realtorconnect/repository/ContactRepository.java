package com.kotyk.realtorconnect.repository;

import com.kotyk.realtorconnect.entity.realtor.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findAllByRealtorId(long realtorId);

}
