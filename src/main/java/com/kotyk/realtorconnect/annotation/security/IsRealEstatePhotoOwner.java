package com.kotyk.realtorconnect.annotation.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@permissionService.isRealEstatePhotoOwner(#realEstatePhotoId) or hasAuthority('MANAGE_REALTOR_INFO')")
public @interface IsRealEstatePhotoOwner {
}
