package com.marketplace.member.exception;

import com.marketplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when login credentials are invalid
 */
public class InvalidCredentialsException extends BaseException {

    public InvalidCredentialsException() {
        super("Invalid username or password",
                HttpStatus.UNAUTHORIZED.value(),
                "INVALID_CREDENTIALS");
    }
}
