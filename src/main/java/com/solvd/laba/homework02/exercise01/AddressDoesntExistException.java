package com.solvd.laba.homework02.exercise01;

public class AddressDoesntExistException extends Exception {
    public AddressDoesntExistException() {
        super();
    }

    public AddressDoesntExistException(String message) {
        super(message);
    }

    public AddressDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
