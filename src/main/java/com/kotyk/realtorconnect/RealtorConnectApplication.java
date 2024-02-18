package com.kotyk.realtorconnect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
@EnableAsync
@EnableAspectJAutoProxy
@SpringBootApplication
public class RealtorConnectApplication {

    public static void main(String[] args) {
        Environment env = SpringApplication.run(RealtorConnectApplication.class, args).getEnvironment();
        changeDefaultTimeZone();
        printSwaggerMessage(env);
    }

    private static void changeDefaultTimeZone() {
        log.info("Setting system timezone to UTC...");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        log.info(String.format("Current system timezone is now %s", TimeZone.getDefault().getID()));
        log.info(String.format("Current system time is %s", LocalDateTime.now()));
    }

    private static void printSwaggerMessage(Environment env) {
        log.info("""
                        \n----------------------------------------------------------
                        \tApplication is running!
                        \tSwagger: \t{}\s
                        ----------------------------------------------------------""",
                env.getProperty("network.swagger-ui-url")
        );
    }

}
