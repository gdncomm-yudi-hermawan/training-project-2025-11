package com.marketplace.member.exception;

import com.marketplace.common.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a user is not found
 */
public class UserNotFoundException extends BaseException {

    public UserNotFoundException(String username) {
        super("User not found: " + username,
                HttpStatus.NOT_FOUND.value(),
                "USER_NOT_FOUND");
    }
}
