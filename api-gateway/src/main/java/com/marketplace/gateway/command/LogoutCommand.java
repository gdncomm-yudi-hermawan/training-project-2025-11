package com.marketplace.gateway.command;

import com.marketplace.common.command.ReactiveCommand;
import reactor.core.publisher.Mono;

public class LogoutCommand implements ReactiveCommand<Void> {

    @Override
    public Mono<Void> execute() {
        // Logout is handled by cookie invalidation in controller
        // This command serves as a placeholder for consistency
        return Mono.empty();
    }
}
