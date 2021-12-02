package com.throttling.task;

public class AccessException extends RuntimeException {
    public AccessException() {
    }

    public AccessException(String message) {
        super(message);
    }
}
