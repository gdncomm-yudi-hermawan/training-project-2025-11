package com.marketplace.gateway.exception;

import com.marketplace.common.dto.ErrorResponse;
import com.marketplace.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;

/**
 * Global exception handler for API Gateway (WebFlux)
 */
@Slf4j
@Component
@Order(-1)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Error occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse;
        HttpStatus status;

        if (ex instanceof BaseException) {
            BaseException baseEx = (BaseException) ex;
            status = HttpStatus.valueOf(baseEx.getStatusCode());
            errorResponse = ErrorResponse.of(
                    baseEx.getStatusCode(),
                    baseEx.getErrorCode(),
                    baseEx.getMessage(),
                    exchange.getRequest().getPath().value());
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorResponse = ErrorResponse.of(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "GATEWAY_ERROR",
                    "An error occurred in the API Gateway",
                    exchange.getRequest().getPath().value());
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            log.error("Error writing response", e);
            return exchange.getResponse().setComplete();
        }
    }
}
