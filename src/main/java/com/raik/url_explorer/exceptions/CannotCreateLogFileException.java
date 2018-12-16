package com.raik.url_explorer.exceptions;

/**
 * @author Raik Yauheni    16.12.2018.
 */
public class CannotCreateLogFileException extends Exception {

    public CannotCreateLogFileException() {
        super();
    }

    public CannotCreateLogFileException(Throwable cause) {
        super(cause);
    }

    public CannotCreateLogFileException(String message) {
        super(message);
    }

    public CannotCreateLogFileException(String message, Throwable cause) {
        super(message, cause);
    }

}
