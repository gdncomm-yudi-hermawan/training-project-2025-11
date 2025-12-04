package com.marketplace.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request DTO for member registration.
 * Email is used as the login identifier.
 */
@Data
public class RegisterRequest {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @com.marketplace.member.validation.ValidPassword
    private String password;

    private String fullName;
    private String address;
    private String phoneNumber;
}
