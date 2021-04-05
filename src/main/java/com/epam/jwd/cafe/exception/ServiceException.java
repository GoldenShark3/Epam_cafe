package com.epam.jwd.cafe.exception;

/**
 * The exception can be thrown when services catch {@link DaoException}
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
