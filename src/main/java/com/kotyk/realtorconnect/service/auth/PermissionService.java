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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PermissionService {

    private final PermissionService proxy;

    private final UserRepository userRepository;
    private final RealEstateRepository realEstateRepository;
    private final RealEstatePhotoRepository realEstatePhotoRepository;
    private final ContactRepository contactRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "getUser", key = "#username")
    public Optional<User> getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "getRealEstate", key = "#realEstateId")
    public Optional<RealEstate> getRealEstate(long realEstateId) {
        return realEstateRepository.findById(realEstateId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "getContact", key = "#contactId")
    public Optional<Contact> getContact(long contactId) {
        return contactRepository.findById(contactId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "getPhoto", key = "#realEstatePhotoId")
    public Optional<RealEstatePhoto> getPhoto(long realEstatePhotoId) {
        return realEstatePhotoRepository.findById(realEstatePhotoId);
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

    public boolean isSameUser(long id) {
        return proxy.getUser(getCurrentUsername())
                .orElse(User.builder().id(-1L).build())
                .getId()
                .equals(id);
    }

    public boolean isRealEstateOwner(long realEstateId) {
        Optional<RealEstate> realEstate = proxy.getRealEstate(realEstateId);
        return realEstate.isPresent() && isSameUser(realEstate.get().getRealtor().getId());
    }

    public boolean isContactOwner(long contactId) {
        Optional<Contact> contact = proxy.getContact(contactId);
        return contact.isPresent() && isSameUser(contact.get().getRealtor().getId());
    }

    public boolean isRealEstatePublic(long realEstateId) {
        Optional<RealEstate> realEstate = proxy.getRealEstate(realEstateId);
        return realEstate.isPresent() && !realEstate.get().isPrivate();
    }

    public boolean isRealEstatePhotoOwner(long realEstatePhotoId) {
        Optional<RealEstatePhoto> photo = proxy.getPhoto(realEstatePhotoId);
        return photo.isPresent() && isSameUser(photo.get().getRealEstate().getRealtor().getId());
    }

}
