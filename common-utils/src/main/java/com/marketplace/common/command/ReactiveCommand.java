package com.marketplace.common.command;

import reactor.core.publisher.Mono;

/**
 * Reactive Command interface for Spring WebFlux controllers.
 * 
 * @param <R> the return type of the command execution
 */
public interface ReactiveCommand<R> {
    Mono<R> execute();
}
