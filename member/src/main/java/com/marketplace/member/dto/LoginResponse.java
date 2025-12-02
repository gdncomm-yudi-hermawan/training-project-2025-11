package com.marketplace.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private String type;
    private Long id;
    private String username;
    private String email;
}
