package com.raik.url_explorer.exceptions;

/**
 * @author Raik Yauheni    16.12.2018.
 */
public class WrongArgumentsException extends Exception {

    public WrongArgumentsException() {
        super();
    }

    public WrongArgumentsException(Throwable cause) {
        super(cause);
    }

    public WrongArgumentsException(String message) {
        super(message);
    }

    public WrongArgumentsException(String message, Throwable cause) {
        super(message, cause);
    }

}
