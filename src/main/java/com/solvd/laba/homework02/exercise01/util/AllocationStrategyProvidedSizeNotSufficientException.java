package com.solvd.laba.homework02.exercise01.util;

public class AllocationStrategyProvidedSizeNotSufficientException extends RuntimeException {
    public AllocationStrategyProvidedSizeNotSufficientException() {
        super();
    }

    public AllocationStrategyProvidedSizeNotSufficientException(String message) {
        super(message);
    }

    public AllocationStrategyProvidedSizeNotSufficientException(String message, Throwable cause) {
        super(message, cause);
    }
}
