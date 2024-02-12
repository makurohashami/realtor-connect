package com.kotyk.realtorconnect.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@permissionService.isRealEstatePublic(#realEstateId) or hasAuthority('SEE_PRIVATE_REAL_ESTATES')")
public @interface IsRealEstatePublic {
}
