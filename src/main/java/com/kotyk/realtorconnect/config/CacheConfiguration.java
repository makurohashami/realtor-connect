package com.kotyk.realtorconnect.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Getter
@Setter
@EnableCaching
@Configuration
@ConfigurationProperties(prefix = "cache")
public class CacheConfiguration {

    private boolean enabled;
    private Map<String, Long> settings;

    @Bean
    CacheManager cacheManager() {
        List<CaffeineCache> caches = new ArrayList<>(settings.size());
            settings.forEach((name, ttl) ->
                    caches.add(new CaffeineCache(name, Caffeine.newBuilder()
                            .expireAfterWrite(enabled ? ttl : 0, TimeUnit.MILLISECONDS).build()))
            );
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);
        return cacheManager;
    }

}
