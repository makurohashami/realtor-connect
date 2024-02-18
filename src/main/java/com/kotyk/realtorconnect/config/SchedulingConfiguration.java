package com.kotyk.realtorconnect.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Getter
@Setter
@Configuration
@EnableScheduling
@ConfigurationProperties(prefix = "scheduler")
@ConditionalOnProperty(name = "scheduler.enabled", havingValue = "true")
public class SchedulingConfiguration implements SchedulingConfigurer {

    private int taskPoolSize;
    private String threadNamePrefix;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

        taskScheduler.setPoolSize(taskPoolSize);
        taskScheduler.setThreadNamePrefix(threadNamePrefix);

        taskScheduler.initialize();
        taskRegistrar.setTaskScheduler(taskScheduler);
    }

}
