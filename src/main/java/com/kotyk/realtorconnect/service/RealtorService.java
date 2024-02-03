package com.kotyk.realtorconnect.service;

import com.kotyk.realtorconnect.dto.realtor.RealtorAddDto;
import com.kotyk.realtorconnect.dto.realtor.RealtorDto;
import com.kotyk.realtorconnect.dto.realtor.RealtorFilter;
import com.kotyk.realtorconnect.dto.realtor.RealtorFullDto;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.entity.realtor.SubscriptionType;
import com.kotyk.realtorconnect.mapper.RealtorMapper;
import com.kotyk.realtorconnect.repository.RealtorRepository;
import com.kotyk.realtorconnect.specification.RealtorFilterSpecifications;
import com.kotyk.realtorconnect.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealtorService {

    public static final String NOT_FOUND_BY_ID_MSG = "Realtor with id '%d' not found";

    private final RealtorMapper realtorMapper;
    private final RealtorRepository realtorRepository;

    @Transactional
    public RealtorFullDto create(RealtorAddDto dto) {
        log.debug("create() - start. dto - {}", dto);
        RealtorFullDto created = realtorMapper.toFullDto(realtorRepository.save(realtorMapper.toEntity(dto)));
        log.debug("create() - end. result = {}", created);
        return created;
    }

    @Transactional(readOnly = true)
    public RealtorFullDto readFullById(long id) {
        log.debug("readFullById() - start. id - {}", id);
        RealtorFullDto dto = realtorMapper.toFullDto(realtorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id))));
        log.debug("readFullById() - end. result = {}", dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public RealtorDto readShortById(long id) {
        log.debug("readShortById() - start. id - {}", id);
        RealtorDto dto = realtorMapper.toDto(realtorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id))));
        log.debug("readShortById() - end. result = {}", dto);
        return dto;
    }

    public Page<RealtorDto> getAllShorts(RealtorFilter filter, Pageable pageable) {
        log.debug("getAllShorts() - start: filter = {}", filter);
        Specification<Realtor> spec = RealtorFilterSpecifications.withFilter(filter);
        Page<RealtorDto> realtors = realtorRepository.findAll(spec, pageable).map(realtorMapper::toDto);
        log.debug("getAllShorts() - end: page elements = {}", realtors.getTotalElements());
        return realtors;
    }

    @Transactional
    public RealtorFullDto update(long id, RealtorAddDto dto) {
        log.debug("update() - start. dto - {}", dto);
        Realtor toUpdate = realtorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id)));
        RealtorFullDto updated = realtorMapper.toFullDto(realtorMapper.update(toUpdate, dto));
        log.debug("update() - end. result = {}", updated);
        return updated;
    }

    @Transactional
    public void delete(long id) {
        log.debug("delete() - start. id - {}", id);
        realtorRepository.deleteById(id);
        log.debug("delete() - end. deleted");
    }

    @Transactional
    public Instant givePremiumToRealtor(long id, short durationInMonths) {
        log.debug("givePremiumToRealtor() - start. realtorId - {}, durationInMonths - {}", id, durationInMonths);
        Realtor realtor = realtorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id)));
        realtor.setSubscriptionType(SubscriptionType.PREMIUM);
        if (realtor.getPremiumExpiresAt() == null) {
            realtor.setPremiumExpiresAt(ZonedDateTime.now(ZoneOffset.UTC).withHour(0).withMinute(0)
                    .withSecond(0).withNano(0).toInstant());
        }
        realtor.setPremiumExpiresAt(ZonedDateTime.ofInstant(realtor.getPremiumExpiresAt(), ZoneOffset.UTC)
                .plusMonths(durationInMonths).toInstant());
        realtorRepository.save(realtor);
        log.debug("givePremiumToRealtor() - end. premium expires at = {}", realtor.getPremiumExpiresAt());
        return realtor.getPremiumExpiresAt();
    }

    @Transactional
    @Scheduled(cron = "${realtor.scheduler.reset-plan-cron}")
    protected void setFreeSubscriptionWhenPrivateExpired() {
        log.debug("setFreeSubscriptionWhenPrivateExpired() - start.");
        List<Realtor> realtors = realtorRepository.findAllByPremiumExpiresAtBefore(Instant.now(Clock.system(ZoneOffset.UTC)));
        realtors.forEach(realtor -> {
            realtor.setPremiumExpiresAt(null);
            realtor.setSubscriptionType(SubscriptionType.FREE);
            log.debug(String.format("Reset subscription for realtor: '%d'", realtor.getId()));
        });
        realtorRepository.saveAll(realtors);
        log.debug("setFreeSubscriptionWhenPrivateExpired() - end. realtors - {}", realtors.size());
    }

}
