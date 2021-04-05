package com.epam.jwd.cafe.handler;

/**
 * The abstract class of {@link Handler} that uses chain of responsibility pattern
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public abstract class AbstractHandler implements Handler {
    protected final Handler nextHandler;

    public AbstractHandler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public AbstractHandler() {
        nextHandler = null;
    }
}
