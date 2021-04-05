package com.epam.jwd.cafe.exception;

/**
 * The exception can be thrown when execution sql query was failed
 * @author Aleksey Vyshamirski
 * @version 1.0.0
 */
public class DaoException extends Exception {
    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }
}
