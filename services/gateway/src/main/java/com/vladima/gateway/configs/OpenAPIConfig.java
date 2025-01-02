package com.vladima.gateway.configs;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class OpenAPIConfig {
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/client-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://client-service"))
                .route(r -> r.path("/product-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://product-service"))
                .route(r -> r.path("/rental-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://rental-service"))
                .build();
    }
}
