package com.kotyk.realtorconnect.util.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class AppLoggerInterceptor {

    @Value("${logging.customLogger.level}")
    private Level level;

    @Pointcut("within(@com.kotyk.realtorconnect.annotation.Loggable *)")
    public void beanAnnotatedWithLoggable() {
    }

    @Pointcut("@annotation(com.kotyk.realtorconnect.annotation.Loggable.Exclude)")
    public void methodNotAnnotatedWithLoggableExclude() {
    }

    @Pointcut("execution(private * *(..))")
    public void privateMethod() {
    }

    @Pointcut("!privateMethod() && beanAnnotatedWithLoggable() && !methodNotAnnotatedWithLoggableExclude()")
    public void publicMethodInsideAClassMarkedWithLoggable() {
    }

    @Before("publicMethodInsideAClassMarkedWithLoggable()")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            log.atLevel(level).log("{} - start. Arguments - {}", methodName, Arrays.toString(args));
        } else {
            log.atLevel(level).log("{} - start", methodName);
        }
    }

    @AfterReturning(value = "publicMethodInsideAClassMarkedWithLoggable()", returning = "returnValue")
    public void logAfter(JoinPoint joinPoint, Object returnValue) {
        String methodName = joinPoint.getSignature().toShortString();
        Object outputValue;
        if (returnValue instanceof Collection) {
            outputValue = "size - " + ((Collection<?>) returnValue).size();
        } else if (returnValue instanceof Page<?>) {
            outputValue = String.format("%s, content size - %d", ((Page<?>) returnValue).getPageable(), ((Page<?>) returnValue).getContent().size());
        } else if (returnValue instanceof Map<?, ?>) {
            outputValue = String.format("size - %d", ((Map<?, ?>) returnValue).size());
        } else {
            outputValue = returnValue;
        }
        log.atLevel(level).log("{} - end. Returned: {}", methodName, outputValue);
    }

}
