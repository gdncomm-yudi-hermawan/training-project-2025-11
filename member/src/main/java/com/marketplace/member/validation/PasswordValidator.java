package com.marketplace.member.validation;

import com.marketplace.member.config.MemberConfigProperties;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validator for password strength
 */
@Component
@RequiredArgsConstructor
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private final MemberConfigProperties memberConfigProperties;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        int minLength = memberConfigProperties.getSecurity().getPasswordMinLength();

        if (password.length() < minLength) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Password must be at least " + minLength + " characters long").addConstraintViolation();
            return false;
        }

        // Add more password rules here if needed (e.g., special chars, numbers)
        // For now, we stick to length as configured in properties

        return true;
    }
}
