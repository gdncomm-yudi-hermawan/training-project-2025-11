package com.marketplace.common.controller;

import com.marketplace.common.command.Command;
import com.marketplace.common.command.CommandInvoker;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {

    @Autowired
    protected CommandInvoker commandInvoker;

    protected <R> R executeCommand(Command<R> command) {
        return commandInvoker.executeCommand(command);
    }
}
