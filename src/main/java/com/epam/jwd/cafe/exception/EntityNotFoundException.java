package com.epam.jwd.cafe.exception;

/**
 * The exception can be thrown when entity in database is not represented
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
