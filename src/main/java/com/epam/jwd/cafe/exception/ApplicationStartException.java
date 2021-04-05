package com.epam.jwd.cafe.exception;

/**
 * The exception can be thrown when application start up is failed
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ApplicationStartException extends RuntimeException {

    public ApplicationStartException(String message) {
        super(message);
    }
}
