package com.solvd.laba.homework11.exercise02;

public class ClosedConnectionException extends RuntimeException {
    public ClosedConnectionException() {
        super();
    }

    public ClosedConnectionException(String message) {
        super(message);
    }

    public ClosedConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
