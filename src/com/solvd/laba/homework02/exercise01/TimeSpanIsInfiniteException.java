package com.solvd.laba.homework02.exercise01;

public class TimeSpanIsInfiniteException extends RuntimeException {
    public TimeSpanIsInfiniteException() {
        super();
    }

    public TimeSpanIsInfiniteException(String message) {
        super(message);
    }

    public TimeSpanIsInfiniteException(String message, Throwable cause) {
        super(message, cause);
    }
}
