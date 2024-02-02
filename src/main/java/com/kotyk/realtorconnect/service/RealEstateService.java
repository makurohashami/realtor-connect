package com.kotyk.realtorconnect.service;

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
import com.kotyk.realtorconnect.specification.RealEstateSpecifications;
import com.kotyk.realtorconnect.util.exception.ActionNotAllowedException;
import com.kotyk.realtorconnect.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealEstateService {

    private static final String NOT_FOUND_BY_ID_MSG = "Real Estate with id '%d' not found";

    private final RealEstateMapper realEstateMapper;
    private final RealEstateRepository realEstateRepository;
    private final RealtorConfiguration realtorConfiguration;
    private final RealtorRepository realtorRepository;

    private String getExMessage(long id) {
        return String.format(NOT_FOUND_BY_ID_MSG, id);
    }

    @Transactional
    public RealEstateFullDto create(long realtorId, RealEstateAddDto realEstateDto) {
        log.debug("create() - start. realtorId = {}, realEstateDto = {}", realtorId, realEstateDto);
        Realtor realtor = realtorRepository.findById(realtorId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(RealtorService.NOT_FOUND_BY_ID_MSG, realtorId)));
        int updatedTotalCount = realtor.getRealEstatesCount() + 1;
        int updatedPublicCount = realEstateDto.isPrivate() ? realtor.getPublicRealEstatesCount() : realtor.getPublicRealEstatesCount() + 1;
        RealtorConfiguration.PlanProperties counts = realtorConfiguration.getPlanPropertiesByPlan(realtor.getSubscriptionType());
        if (updatedTotalCount > counts.getMaxTotalRealEstates()) {
            throw new ActionNotAllowedException("It is impossible to add a real estate because the maximum number of real estates has been reached");
        }
        if (updatedPublicCount > counts.getMaxPublicRealEstates()) {
            throw new ActionNotAllowedException("It is impossible to add a public real estate because the maximum number of public real estates has been reached");
        }
        RealEstateFullDto realEstate = realEstateMapper.toFullDto(realEstateRepository.save(
                realEstateMapper.toEntity(realEstateDto, realtorId))
        );
        realtorRepository.setRealEstateCountsByRealtorId(realtorId, updatedTotalCount, updatedPublicCount);
        log.debug("create() - end. realEstate = {}", realEstate);
        return realEstate;
    }

    @Transactional(readOnly = true)
    public RealEstateDto readShortById(long realEstateId) {
        log.debug("readShortById() - start. realEstateId = {}", realEstateId);
        RealEstateDto realEstate = realEstateMapper.toDto(realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new ResourceNotFoundException(getExMessage(realEstateId)))
        );
        log.debug("readShortById() - end. realEstate = {}", realEstate);
        return realEstate;
    }

    @Transactional(readOnly = true)
    public RealEstateFullDto readFullById(long realEstateId) {
        log.debug("readFullById() - start. realEstateId = {}", realEstateId);
        RealEstateFullDto realEstate = realEstateMapper.toFullDto(realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new ResourceNotFoundException(getExMessage(realEstateId)))
        );
        log.debug("readFullById() - end. realEstate = {}", realEstate);
        return realEstate;
    }

    @Transactional(readOnly = true)
    public Page<RealEstateDto> readAllShorts(RealEstateFilter filter, Pageable pageable) {
        log.debug("readAllShorts() - start. filter = {}, pageable = {}", filter, pageable);
        Specification<RealEstate> spec = RealEstateSpecifications.withFilter(filter);
        Page<RealEstateDto> realEstates = realEstateRepository.findAll(spec, pageable).map(realEstateMapper::toDto);
        log.debug("readAllShorts() - end: size = {}", realEstates.getTotalElements());
        return realEstates;
    }

    @Transactional(readOnly = true)
    public Page<RealEstateFullDto> readAllFulls(RealEstateFilter filter, Pageable pageable) {
        log.debug("readAllFulls() - start. filter = {}, pageable = {}", filter, pageable);
        Specification<RealEstate> spec = RealEstateSpecifications.withFilter(filter);
        Page<RealEstateFullDto> realEstates = realEstateRepository.findAll(spec, pageable).map(realEstateMapper::toFullDto);
        log.debug("readAllFulls() - end: size = {}", realEstates.getTotalElements());
        return realEstates;
    }

    @Transactional
    public RealEstateFullDto update(long realEstateId, RealEstateAddDto realEstateDto) {
        log.debug("update() - start. realEstateId = {}, realEstateDto = {}", realEstateId, realEstateDto);
        RealEstate toUpdate = realEstateRepository.findById(realEstateId)
                .orElseThrow(() -> new ResourceNotFoundException(getExMessage(realEstateId)));
        if (toUpdate.isPrivate() != realEstateDto.isPrivate()) {
            Realtor realtor = toUpdate.getRealtor();
            int updatedPublicCount = realEstateDto.isPrivate() ? realtor.getPublicRealEstatesCount() - 1 : realtor.getPublicRealEstatesCount() + 1;
            if (!realEstateDto.isPrivate() && updatedPublicCount > realtorConfiguration.getPlanPropertiesByPlan(realtor.getSubscriptionType()).getMaxPublicRealEstates()) {
                throw new ActionNotAllowedException("It is impossible to make an object public because the maximum number of public real estates has been reached");
            }
            realtorRepository.setRealEstateCountsByRealtorId(realtor.getId(), realtor.getRealEstatesCount(), updatedPublicCount);
        }
        RealEstateFullDto updated = realEstateMapper.toFullDto(realEstateMapper.update(toUpdate, realEstateDto));
        log.debug("update() - end. updated = {}", updated);
        return updated;
    }

    @Transactional
    public void delete(long realEstateId) {
        log.debug("delete() - start. realEstateId = {}", realEstateId);
        Optional<RealEstate> optionalRealEstate = realEstateRepository.findById(realEstateId);
        if (optionalRealEstate.isPresent()) {
            RealEstate realEstate = optionalRealEstate.get();
            realEstateRepository.deleteById(realEstateId);
            realtorRepository.setRealEstateCountsByRealtorId(
                    realEstate.getRealtor().getId(),
                    realEstate.getRealtor().getRealEstatesCount() - 1,
                    realEstate.isPrivate() ? realEstate.getRealtor().getPublicRealEstatesCount() : realEstate.getRealtor().getPublicRealEstatesCount() - 1
            );
        }
        log.debug("delete() - end. deleted");
    }

}