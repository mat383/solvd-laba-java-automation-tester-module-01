package com.solvd.laba.homework02.exercise01;

public class TimeSpanHasNegativeDurationError extends RuntimeException {
    public TimeSpanHasNegativeDurationError() {
        super();
    }

    public TimeSpanHasNegativeDurationError(String message) {
        super(message);
    }

    public TimeSpanHasNegativeDurationError(String message, Throwable cause) {
        super(message, cause);
    }
}
