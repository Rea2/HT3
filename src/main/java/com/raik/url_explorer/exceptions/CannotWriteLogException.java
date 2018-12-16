package com.raik.url_explorer.exceptions;

/**
 * @author Raik Yauheni    16.12.2018.
 */
public class CannotWriteLogException extends Exception {

    public CannotWriteLogException() {
        super();
    }

    public CannotWriteLogException(Throwable cause) {
        super(cause);
    }

    public CannotWriteLogException(String message) {
        super(message);
    }

    public CannotWriteLogException(String message, Throwable cause) {
        super(message, cause);
    }

}
