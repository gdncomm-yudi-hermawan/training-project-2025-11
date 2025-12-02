package com.marketplace.member.exception;

import com.marketplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user already exists (duplicate username or email)
 */
public class UserAlreadyExistsException extends BaseException {

    public UserAlreadyExistsException(String message) {
        super(message,
                HttpStatus.CONFLICT.value(),
                "USER_ALREADY_EXISTS");
    }

    public static UserAlreadyExistsException username(String username) {
        return new UserAlreadyExistsException("Username already exists: " + username);
    }

    public static UserAlreadyExistsException email(String email) {
        return new UserAlreadyExistsException("Email already exists: " + email);
    }
}
