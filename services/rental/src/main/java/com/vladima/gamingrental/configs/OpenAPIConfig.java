package com.vladima.gamingrental.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI(
            @Value("${openapi.title}") String title,
            @Value("${openapi.description}") String description,
            @Value("${openapi.version}") String version,
            @Value("${openapi.url}") String url
    ) {
        return new OpenAPI()
                .servers(List.of(new Server().url(url)))
                .info(new Info().title(title)
                        .description(description)
                        .version(version));
    }
}
