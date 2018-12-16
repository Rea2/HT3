package com.raik.url_explorer.exceptions;

/**
 * @author Raik Yauheni    15.12.2018.
 */
public class WrongInstructionException extends Exception {

    public WrongInstructionException() {
        super();
    }

    public WrongInstructionException(Throwable cause) {
        super(cause);
    }

    public WrongInstructionException(String message) {
        super(message);
    }

    public WrongInstructionException(String message, Throwable cause) {
        super(message, cause);
    }

}
