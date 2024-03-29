package com.kotyk.realtorconnect.service.auth;

import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.realestate.RealEstatePhoto;
import com.kotyk.realtorconnect.entity.realtor.Contact;
import com.kotyk.realtorconnect.entity.user.Permission;
import com.kotyk.realtorconnect.entity.user.User;
import com.kotyk.realtorconnect.repository.ContactRepository;
import com.kotyk.realtorconnect.repository.RealEstatePhotoRepository;
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
    private final RealEstatePhotoRepository realEstatePhotoRepository;
    private final ContactRepository contactRepository;

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElse(User.builder().id(-1L).build());
    }

    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public boolean isCurrentHasPermission(Permission permission) {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .contains(new SimpleGrantedAuthority(permission.name()));
    }

    @Transactional(readOnly = true)
    public boolean isSameUser(long id) {
        return getUser(getCurrentUsername()).getId().equals(id);
    }

    @Transactional(readOnly = true)
    public boolean isRealEstateOwner(long realEstateId) {
        Optional<RealEstate> realEstate = realEstateRepository.findById(realEstateId);
        return realEstate.isPresent() && isSameUser(realEstate.get().getId());
    }

    @Transactional(readOnly = true)
    public boolean isContactOwner(long contactId) {
        Optional<Contact> contact = contactRepository.findById(contactId);
        return contact.isPresent() && isSameUser(contact.get().getRealtor().getId());
    }

    @Transactional(readOnly = true)
    public boolean isRealEstatePublic(long realEstateId) {
        Optional<RealEstate> realEstate = realEstateRepository.findById(realEstateId);
        return realEstate.isPresent() && !realEstate.get().isPrivate();
    }

    @Transactional(readOnly = true)
    public boolean isRealEstatePhotoOwner(long realEstatePhotoId) {
        Optional<RealEstatePhoto> photo = realEstatePhotoRepository.findById(realEstatePhotoId);
        return photo.isPresent() && isSameUser(photo.get().getRealEstate().getRealtor().getId());
    }


}
