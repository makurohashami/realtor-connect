package com.kotyk.realtorconnect.service.realestate;

import com.kotyk.realtorconnect.annotation.datafilter.RealEstatesFiltered;
import com.kotyk.realtorconnect.annotation.datafilter.RealEstatesPhotoFiltered;
import com.kotyk.realtorconnect.config.RealEstateConfiguration;
import com.kotyk.realtorconnect.config.RealtorConfiguration;
import com.kotyk.realtorconnect.dto.realestate.RealEstateAddDto;
import com.kotyk.realtorconnect.dto.realestate.RealEstateDto;
import com.kotyk.realtorconnect.dto.realestate.RealEstateFilter;
import com.kotyk.realtorconnect.dto.realestate.RealEstateFullDto;
import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.mapper.RealEstateMapper;
import com.kotyk.realtorconnect.repository.RealEstateRepository;
import com.kotyk.realtorconnect.repository.RealtorRepository;
import com.kotyk.realtorconnect.service.realtor.RealtorService;
import com.kotyk.realtorconnect.specification.RealEstateSpecifications;
import com.kotyk.realtorconnect.util.exception.ActionNotAllowedException;
import com.kotyk.realtorconnect.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealEstateService {

    public static final String NOT_FOUND_BY_ID_MSG = "Real Estate with id '%d' not found";

    private final RealEstateMapper realEstateMapper;
    private final RealEstateRepository realEstateRepository;
    private final RealEstateConfiguration realEstateConfiguration;

    private final RealtorConfiguration realtorConfiguration;
    private final RealtorRepository realtorRepository;

    private String getExMessage(long id) {
        return String.format(NOT_FOUND_BY_ID_MSG, id);
    }

    @Transactional
    public RealEstateFullDto create(long realtorId, RealEstateAddDto realEstateDto) {
        Realtor realtor = realtorRepository.findById(realtorId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(RealtorService.NOT_FOUND_BY_ID_MSG, realtorId)));
        int updatedPublicCount = realEstateDto.isPrivate() ? realtor.getPublicRealEstatesCount() : realtor.getPublicRealEstatesCount() + 1;
        long maxCounts = realtorConfiguration.getPlanPropertiesByPlan(realtor.getSubscriptionType()).getMaxPublicRealEstates();
        if (updatedPublicCount > maxCounts) {
            throw new ActionNotAllowedException("It is impossible to add a public real estate because the maximum number of public real estates has been reached");
        }
        realtorRepository.setRealEstateCountsByRealtorId(realtorId, updatedPublicCount);
        return realEstateMapper.toFullDto(realEstateRepository.save(
                realEstateMapper.toEntity(realEstateDto, realtorId))
        );
    }

    @RealEstatesPhotoFiltered
    @Transactional(readOnly = true)
    public RealEstateDto readShortById(long realEstateId) {
        return realEstateMapper.toDto(realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new ResourceNotFoundException(getExMessage(realEstateId))));
    }

    @Transactional(readOnly = true)
    public RealEstateFullDto readFullById(long realEstateId) {
        return realEstateMapper.toFullDto(realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new ResourceNotFoundException(getExMessage(realEstateId)))
        );
    }

    @RealEstatesFiltered
    @RealEstatesPhotoFiltered
    @Transactional(readOnly = true)
    public Page<RealEstateDto> readAllShorts(RealEstateFilter filter, Pageable pageable) {
        Specification<RealEstate> spec = RealEstateSpecifications.withFilter(filter);
        return realEstateRepository.findAll(spec, pageable).map(realEstateMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<RealEstateFullDto> readAllFulls(RealEstateFilter filter, Pageable pageable) {
        Specification<RealEstate> spec = RealEstateSpecifications.withFilter(filter);
        return realEstateRepository.findAll(spec, pageable).map(realEstateMapper::toFullDto);
    }

    @Transactional
    public RealEstateFullDto update(long realEstateId, RealEstateAddDto realEstateDto) {
        RealEstate toUpdate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new ResourceNotFoundException(getExMessage(realEstateId)));
        if (toUpdate.isPrivate() != realEstateDto.isPrivate()) {
            Realtor realtor = toUpdate.getRealtor();
            int updatedPublicCount = realEstateDto.isPrivate() ? realtor.getPublicRealEstatesCount() - 1 : realtor.getPublicRealEstatesCount() + 1;
            if (!realEstateDto.isPrivate() && updatedPublicCount > realtorConfiguration.getPlanPropertiesByPlan(realtor.getSubscriptionType()).getMaxPublicRealEstates()) {
                throw new ActionNotAllowedException("It is impossible to make an object public because the maximum number of public real estates has been reached");
            }
            realtorRepository.setRealEstateCountsByRealtorId(realtor.getId(), updatedPublicCount);
        }
        return realEstateMapper.toFullDto(realEstateMapper.update(toUpdate, realEstateDto));
    }

    @Transactional
    public void delete(long realEstateId) {
        Optional<RealEstate> optionalRealEstate = realEstateRepository.findById(realEstateId);
        if (optionalRealEstate.isPresent()) {
            RealEstate realEstate = optionalRealEstate.get();
            realEstateRepository.deleteById(realEstateId);
            realtorRepository.setRealEstateCountsByRealtorId(
                    realEstate.getRealtor().getId(),
                    realEstate.isPrivate() ? realEstate.getRealtor().getPublicRealEstatesCount() : realEstate.getRealtor().getPublicRealEstatesCount() - 1
            );
        }
    }

    @Transactional
    public boolean updateVerified(long realEstateId, boolean verified) {
        RealEstate realEstate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new ResourceNotFoundException(getExMessage(realEstateId)));
        realEstate.setVerified(verified);
        return realEstate.isVerified();
    }

    @Transactional
    public boolean updateCalled(long realEstateId, boolean called) {
        RealEstate realEstate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new ResourceNotFoundException(getExMessage(realEstateId)));
        realEstate.setCalled(called);
        realEstate.setCalledAt(Instant.now());
        return realEstate.isCalled();
    }

    @Transactional
    @Scheduled(fixedRateString = "${real-estate.scheduler.check-called}")
    protected void setNotCalledWhenCalledAtExpired() {
        Instant time = ZonedDateTime.now().minusDays(realEstateConfiguration.getDaysForExpireCalled()).toInstant();
        List<RealEstate> realEstates = realEstateRepository.findAllByCalledAtBeforeAndCalledTrue(time);
        realEstates.forEach(realEstate -> realEstate.setCalled(false));
    }

}
