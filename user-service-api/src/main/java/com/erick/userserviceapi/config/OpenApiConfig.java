package com.erick.userserviceapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiConfig {

    @Value("${springdoc.openapi.title}")
    private String title;

    @Value("${springdoc.openapi.description}")
    private String description;

    @Value("${springdoc.openapi.version}")
    private String version;

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(
                new Info()
                        .title(title)
                        .description(description)
                        .version(version)
        );
    }

}
