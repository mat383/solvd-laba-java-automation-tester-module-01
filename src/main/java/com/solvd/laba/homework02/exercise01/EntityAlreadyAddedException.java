package com.solvd.laba.homework02.exercise01;

public class EntityAlreadyAddedException extends RuntimeException {
    public EntityAlreadyAddedException() {
        super();
    }

    public EntityAlreadyAddedException(String message) {
        super(message);
    }

    public EntityAlreadyAddedException(String message, Throwable cause) {
        super(message, cause);
    }
}
