package com.example.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("customer-service", r -> r
                        .path("/customer-service/**")
                        .uri("lb://CUSTOMER-SERVICE"))

                .route("product-service", r -> r
                        .path("/product-service/**")
                        .uri("lb://PRODUCT-SERVICE"))

                .route("order-service", r -> r
                        .path("/v1/order-api/**")
                        .filters(f -> f.rewritePath(
                                "/v1/order-api/(?<segment>.*)",
                                "/api/v1/orders/${segment}"
                        ))
                        .uri("lb://ORDER-SERVICE"))

                .build();
    }
}
