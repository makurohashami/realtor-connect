package com.kotyk.realtorconnect.service.realestate;

import com.kotyk.realtorconnect.dto.realestate.RealEstateDto;
import com.kotyk.realtorconnect.dto.realestate.photo.RealEstatePhotoDto;
import com.kotyk.realtorconnect.entity.user.Permission;
import com.kotyk.realtorconnect.service.auth.PermissionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class RealEstateFilterInterceptor {

    private final PermissionService permissionService;

    @Pointcut("@annotation(com.kotyk.realtorconnect.annotation.datafilter.RealEstatesFiltered)")
    public void filterRealEstatesPointcut() {
    }

    @Around("filterRealEstatesPointcut()")
    public Object filterRealEstates(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof Page<?> pageResult) {
            if (pageResult.hasContent() && pageResult.getContent().get(0) instanceof RealEstateDto) {
                return filterPageRealEstateDto((Page<RealEstateDto>) pageResult);
            }
        }

        log.warn("Cannot filter real estates because returned unsupported type: {}", result.getClass().getSimpleName());
        return result;
    }

    private Page<RealEstateDto> filterPageRealEstateDto(Page<RealEstateDto> realEstatePage) {
        if (permissionService.isCurrentHasPermission(Permission.SEE_PRIVATE_REAL_ESTATES)) {
            return realEstatePage;
        }
        List<RealEstateDto> filtered = realEstatePage.getContent().stream()
                .filter(realEstate -> !realEstate.isPrivate()).toList();
        return new PageImpl<>(filtered, realEstatePage.getPageable(), realEstatePage.getTotalElements());
    }

    @Pointcut("@annotation(com.kotyk.realtorconnect.annotation.datafilter.RealEstatesPhotoFiltered)")
    public void filterRealEstatePhotosPointcut() {
    }

    @AfterReturning(pointcut = "filterRealEstatePhotosPointcut()", returning = "result")
    public void filterRealEstatePhotos(Object result) {
        if (result instanceof RealEstateDto realEstateDto) {
            filterPhotosInRealEstateDto(realEstateDto);
        } else if (result instanceof Page<?> pageResult) {
            if (pageResult.hasContent() && pageResult.getContent().get(0) instanceof RealEstateDto) {
                filterRealEstatePhotosDtoPage((Page<RealEstateDto>) pageResult);
            }
        } else if (result instanceof List<?> listResult) {
            if (!CollectionUtils.isEmpty(listResult)) {
                filterRealEstatePhotosDtoList((List<RealEstatePhotoDto>) listResult);
            }
        } else {
            log.warn("Cannot filter real estates photos because returned unsupported type: {}", result.getClass().getSimpleName());
        }
    }

    private void filterPhotosInRealEstateDto(RealEstateDto realEstateDto) {
        if (realEstateDto.getPhotos() == null) {
            return;
        }
        if (!permissionService.isCurrentHasPermission(Permission.SEE_PRIVATE_PHOTOS)) {
            realEstateDto.setPhotos(
                    realEstateDto.getPhotos().stream()
                            .filter(photo -> !photo.isPrivate())
                            .toList()
            );
        }
    }

    private void filterRealEstatePhotosDtoPage(Page<RealEstateDto> realEstatePage) {
        realEstatePage.getContent().forEach(this::filterPhotosInRealEstateDto);
    }


    private void filterRealEstatePhotosDtoList(List<RealEstatePhotoDto> photos) {
        if (!permissionService.isCurrentHasPermission(Permission.SEE_PRIVATE_PHOTOS)) {
            photos.removeIf(RealEstatePhotoDto::isPrivate);
        }
    }
}
