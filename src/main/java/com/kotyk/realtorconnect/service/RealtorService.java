package com.kotyk.realtorconnect.service;

import com.kotyk.realtorconnect.annotation.datafilter.ContactsFiltered;
import com.kotyk.realtorconnect.config.RealtorConfiguration;
import com.kotyk.realtorconnect.dto.realtor.RealtorAddDto;
import com.kotyk.realtorconnect.dto.realtor.RealtorDto;
import com.kotyk.realtorconnect.dto.realtor.RealtorFilter;
import com.kotyk.realtorconnect.dto.realtor.RealtorFullDto;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import com.kotyk.realtorconnect.entity.realtor.SubscriptionType;
import com.kotyk.realtorconnect.mapper.RealtorMapper;
import com.kotyk.realtorconnect.repository.RealEstateRepository;
import com.kotyk.realtorconnect.repository.RealtorRepository;
import com.kotyk.realtorconnect.service.email.EmailFacade;
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

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealtorService {

    public static final String NOT_FOUND_BY_ID_MSG = "Realtor with id '%d' not found";

    private final EmailFacade emailFacade;
    private final RealtorMapper realtorMapper;
    private final RealtorRepository realtorRepository;
    private final RealtorConfiguration realtorConfiguration;
    private final RealEstateRepository realEstateRepository;
    private final ConfirmationTokenService tokenService;

    @Transactional
    public RealtorFullDto create(RealtorAddDto dto) {
        Realtor realtor = realtorRepository.save(realtorMapper.toEntity(dto));
        emailFacade.sendVerifyEmail(realtor, tokenService.createToken(realtor));
        return realtorMapper.toFullDto(realtor);
    }

    @Transactional(readOnly = true)
    public RealtorFullDto readFullById(long id) {
        return realtorMapper.toFullDto(realtorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id))));
    }

    @ContactsFiltered
    @Transactional(readOnly = true)
    public RealtorDto readShortById(long id) {
        return realtorMapper.toDto(realtorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id))));
    }

    @ContactsFiltered
    public Page<RealtorDto> getAllShorts(RealtorFilter filter, Pageable pageable) {
        Specification<Realtor> spec = RealtorFilterSpecifications.withFilter(filter);
        return realtorRepository.findAll(spec, pageable).map(realtorMapper::toDto);
    }

    @Transactional
    public RealtorFullDto update(long id, RealtorAddDto dto) {
        Realtor toUpdate = realtorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id)));
        return realtorMapper.toFullDto(realtorMapper.update(toUpdate, dto));
    }

    @Transactional
    public void delete(long id) {
        realtorRepository.deleteById(id);
    }

    @Transactional
    public Instant givePremiumToRealtor(long id, short durationInMonths) {
        Realtor realtor = realtorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND_BY_ID_MSG, id)));
        realtor.setSubscriptionType(SubscriptionType.PREMIUM);
        if (realtor.getPremiumExpiresAt() == null) {
            realtor.setPremiumExpiresAt(ZonedDateTime.now().withHour(0).withMinute(0)
                    .withSecond(0).withNano(0).toInstant());
        }
        realtor.setPremiumExpiresAt(ZonedDateTime.ofInstant(realtor.getPremiumExpiresAt(), ZoneOffset.UTC)
                .plusMonths(durationInMonths).toInstant());
        realtor.setNotifiedDaysToExpirePremium(Integer.MAX_VALUE);
        realtorRepository.save(realtor);
        emailFacade.sendStartPremiumEmail(realtor, durationInMonths);
        return realtor.getPremiumExpiresAt();
    }

    @Transactional
    @Scheduled(cron = "${realtor.scheduler.reset-plan-cron}")
    protected void setFreeSubscriptionWhenPrivateExpired() {
        List<Realtor> realtors = realtorRepository.findAllByPremiumExpiresAtBeforeAndSubscriptionType(Instant.now(), SubscriptionType.PREMIUM);
        realtors.forEach(realtor -> {
            realtor.setPremiumExpiresAt(null);
            realtor.setNotifiedDaysToExpirePremium(null);
            realtor.setSubscriptionType(SubscriptionType.FREE);
            log.debug(String.format("Reset subscription for realtor: '%d'", realtor.getId()));
        });
        List<Long> realtorIds = realtors.stream().map(Realtor::getId).toList();
        realEstateRepository.makeAllRealEstatesPrivateByRealtors(realtorIds);
        realtors.forEach(emailFacade::sendPremiumExpiredEmail);
    }

    @Transactional
    @Scheduled(cron = "${realtor.scheduler.send-email-when-premium-expires-cron}")
    protected void sendEmailWhenLeftOneDayOfPremium() {
        realtorConfiguration.getDaysToNotifyExpiresPremium().forEach(
                daysLeft -> CompletableFuture.runAsync(() -> sendEmailWhenLeftFewDaysOfPremium(daysLeft))
        );
    }

    protected void sendEmailWhenLeftFewDaysOfPremium(int daysLeft) {
        ZonedDateTime time = ZonedDateTime.now().plusDays(daysLeft).withZoneSameInstant(ZoneOffset.UTC);
        realtorRepository.findAllNotNotifiedExpiringPremium(daysLeft, time.getDayOfMonth(), time.getMonthValue(), time.getYear())
                .forEach(realtor -> {
                    emailFacade.sendPremiumExpiresEmail(realtor);
                    realtor.setNotifiedDaysToExpirePremium(daysLeft);
                    realtorRepository.save(realtor);
                });
    }

}
