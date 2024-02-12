package com.kotyk.realtorconnect.service;

import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.realtor.Contact;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.entity.user.Permission;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.repository.ContactRepository;
import com.kotyk.realtorconnect.repository.RealEstateRepository;
import com.kotyk.realtorconnect.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PermissionService {

    private final UserRepository userRepository;
    private final RealEstateRepository realEstateRepository;
    private final ContactRepository contactRepository;

    @Transactional(readOnly = true)
    public boolean isSameUser(long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElse(User.builder().id(-1L).build());
        return user.getId().equals(id);
    }

    @Transactional(readOnly = true)
    public boolean isRealEstateOwner(long realEstateId) {
        RealEstate plug = new RealEstate();
        plug.setRealtor(Realtor.builder().id(-1L).build());
        RealEstate realEstate = realEstateRepository.findById(realEstateId)
                .orElse(plug);
        if (realEstate.getId() == null) {
            return false;
        }
        return isSameUser(realEstate.getRealtor().getId());
    }

    @Transactional(readOnly = true)
    public boolean isContactOwner(long contactId) {
        Contact plug = new Contact();
        plug.setRealtor(Realtor.builder().id(-1L).build());
        Contact contact = contactRepository.findById(contactId)
                .orElse(plug);
        if (contact.getId() == null) {
            return false;
        }
        return isSameUser(contact.getRealtor().getId());
    }

    public boolean isCurrentHasPermission(Permission permission) {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .contains(new SimpleGrantedAuthority(permission.name()));
    }

    public boolean isRealEstatePublic(long realEstateId) {
        Optional<RealEstate> realEstateOpt = realEstateRepository.findById(realEstateId);
        return realEstateOpt.map(realEstate -> !realEstate.isPrivate()).orElse(false);
    }


}
