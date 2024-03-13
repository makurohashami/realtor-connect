package com.kotyk.realtorconnect.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

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

}
