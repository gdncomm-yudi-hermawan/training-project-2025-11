package com.marketplace.gateway.exception;

import com.marketplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when JWT token is invalid or expired
 */
public class InvalidTokenException extends BaseException {

    public InvalidTokenException(String message) {
        super(message,
                HttpStatus.UNAUTHORIZED.value(),
                "INVALID_TOKEN");
    }

    public static InvalidTokenException expired() {
        return new InvalidTokenException("JWT token has expired");
    }

    public static InvalidTokenException malformed() {
        return new InvalidTokenException("JWT token is malformed");
    }

    public static InvalidTokenException missing() {
        return new InvalidTokenException("JWT token is missing");
    }
}
