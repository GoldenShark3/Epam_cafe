package com.epam.jwd.cafe.handler;

public abstract class AbstractHandler implements Handler {
    protected final Handler nextHandler;

    public AbstractHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public AbstractHandler() {
        nextHandler = null;
    }
}
