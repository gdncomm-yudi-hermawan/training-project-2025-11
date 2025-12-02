package com.marketplace.gateway.filter;

import com.marketplace.common.util.JwtUtil;
import com.marketplace.gateway.constant.GatewayConstants;
import com.marketplace.gateway.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * JWT Authentication Filter for API Gateway
 * Validates JWT tokens and adds user information to headers
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.debug("JWT Authentication Filter executing for: {}", exchange.getRequest().getPath());

            String token = extractToken(exchange);

            if (token == null) {
                log.warn("Missing authorization token");
                throw InvalidTokenException.missing();
            }

            try {
                if (!jwtUtil.validateToken(token)) {
                    log.warn("Invalid JWT token");
                    throw InvalidTokenException.malformed();
                }

                String username = jwtUtil.extractUsername(token);
                log.debug("Authenticated user: {}", username);

                // Add username to request headers for downstream services
                ServerWebExchange modifiedExchange = exchange.mutate()
                        .request(r -> r.header(GatewayConstants.Headers.USERNAME, username))
                        .build();

                return chain.filter(modifiedExchange);

            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                log.warn("JWT token expired: {}", e.getMessage());
                throw InvalidTokenException.expired();
            } catch (Exception e) {
                log.error("JWT validation error: {}", e.getMessage(), e);
                throw InvalidTokenException.malformed();
            }
        };
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public static class Config {
        // Configuration properties if needed
    }
}
