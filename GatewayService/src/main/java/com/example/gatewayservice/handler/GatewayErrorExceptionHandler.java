package com.example.gatewayservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(-1)
public class GatewayErrorExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(
            ServerWebExchange exchange,
            Throwable ex) {

        exchange.getResponse().setStatusCode(
                HttpStatus.SERVICE_UNAVAILABLE
        );

        exchange.getResponse().getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", 503);
        errorResponse.put("error", "Service Unavailable");
        errorResponse.put(
                "message",
                "Cổng Gateway không thể kết nối tới dịch vụ đích"
        );

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);

            return exchange.getResponse()
                    .writeWith(
                            Mono.just(
                                    exchange.getResponse()
                                            .bufferFactory()
                                            .wrap(bytes)
                            )
                    );

        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
}