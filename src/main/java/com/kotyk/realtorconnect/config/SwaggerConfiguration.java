package com.kotyk.realtorconnect.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Setter;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Setter
@Configuration
@ConfigurationProperties(prefix = "openapi")
public class SwaggerConfiguration {

    private Map<String, String[]> groups;

    @Bean
    public OpenAPI openApiConfiguration(
            @Value("${openapi.title}") final String title,
            @Value("${openapi.version}") final String version,
            @Value("${openapi.description}") final String description
    ) {
        String label = "JWT Token";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(label))
                .components(new Components().addSecuritySchemes(label, new SecurityScheme()
                        .name(label)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                );
    }

    @Bean
    public GroupedOpenApi allAPIs() {
        return GroupedOpenApi.builder()
                .group("1. All APIs")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userAPIs() {
        return GroupedOpenApi.builder()
                .group("2. User APIs")
                .pathsToMatch(groups.get("userAPIs"))
                .build();
    }

    @Bean
    public GroupedOpenApi realtorAPIs() {
        return GroupedOpenApi.builder()
                .group("3. Realtor APIs")
                .pathsToMatch(groups.get("realtorAPIs"))
                .build();
    }

    @Bean
    public GroupedOpenApi adminAPIs() {
        return GroupedOpenApi.builder()
                .group("4. Admin APIs")
                .pathsToMatch(groups.get("adminAPIs"))
                .build();
    }

}
