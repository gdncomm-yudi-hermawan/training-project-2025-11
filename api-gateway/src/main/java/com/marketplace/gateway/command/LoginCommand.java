package com.marketplace.gateway.command;

import com.marketplace.common.command.ReactiveCommand;
import com.marketplace.gateway.dto.LoginRequest;
import com.marketplace.gateway.dto.LoginResponse;
import com.marketplace.gateway.service.AuthService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginCommand implements ReactiveCommand<LoginResponse> {

    private final AuthService authService;
    private final LoginRequest request;

    @Override
    public Mono<LoginResponse> execute() {
        return authService.login(request);
    }
}
