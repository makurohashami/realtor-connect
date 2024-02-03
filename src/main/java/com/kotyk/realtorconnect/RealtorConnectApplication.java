package com.kotyk.realtorconnect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class RealtorConnectApplication {

    public static void main(String[] args) {
        Environment env = SpringApplication.run(RealtorConnectApplication.class, args).getEnvironment();
        String serverPort = env.getProperty("server.port");
        log.info("""
                        \n----------------------------------------------------------
                        \tApplication is running!
                        \tSwagger: \t{}\s
                        ----------------------------------------------------------""",
                "http://localhost:" + serverPort + "/swagger-ui.html"
        );
    }

}
