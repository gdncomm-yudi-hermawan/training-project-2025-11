package com.marketplace.common.command;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReactiveCommandInvoker {

    public <R> Mono<R> executeCommand(ReactiveCommand<R> command) {
        return command.execute();
    }
}
