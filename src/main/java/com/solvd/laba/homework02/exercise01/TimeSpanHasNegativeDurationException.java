package com.solvd.laba.homework02.exercise01;

public class TimeSpanHasNegativeDurationException extends RuntimeException {
    public TimeSpanHasNegativeDurationException() {
        super();
    }

    public TimeSpanHasNegativeDurationException(String message) {
        super(message);
    }

    public TimeSpanHasNegativeDurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
