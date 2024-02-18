package com.kotyk.realtorconnect.service.contact;

import com.kotyk.realtorconnect.dto.realtor.RealtorDto;
import com.kotyk.realtorconnect.entity.user.Permission;
import com.kotyk.realtorconnect.service.PermissionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class ContactFilterInterceptor {

    private final PermissionService permissionService;

    @Pointcut("@annotation(com.kotyk.realtorconnect.annotation.datafilter.ContactsFiltered)")
    public void filterContactsPointcut() {
    }

    @AfterReturning(pointcut = "filterContactsPointcut()", returning = "result")
    public void filterContacts(Object result) {
        if (result instanceof RealtorDto realtorDto) {
            filterRealtorDto(realtorDto);
        } else if (result instanceof Page<?> pageResult) {
            if (pageResult.hasContent() && pageResult.getContent().get(0) instanceof RealtorDto) {
                filterPageRealtorDto((Page<RealtorDto>) pageResult);
            }
        } else {
            log.debug("Cannot filter contacts because returned unsupported type");
        }
    }

    private void filterRealtorDto(RealtorDto realtor) {
        if (!permissionService.isCurrentHasPermission(Permission.SEE_REALTORS_CONTACTS)) {
            realtor.setContacts(new ArrayList<>());
        }
    }

    private void filterPageRealtorDto(Page<RealtorDto> realtorDtoPage) {
        realtorDtoPage.getContent().forEach(this::filterRealtorDto);
    }

}
