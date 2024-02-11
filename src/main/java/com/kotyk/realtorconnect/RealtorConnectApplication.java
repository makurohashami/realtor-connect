package com.kotyk.realtorconnect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
@SpringBootApplication
public class RealtorConnectApplication {

    public static void main(String[] args) {
        Environment env = SpringApplication.run(RealtorConnectApplication.class, args).getEnvironment();
        String swaggerUrl = env.getProperty("network.swagger-ui-url");
        log.info("""
                        \n----------------------------------------------------------
                        \tApplication is running!
                        \tSwagger: \t{}\s
                        ----------------------------------------------------------""",
                swaggerUrl
        );
    }

}
