package ru.otus.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }

    public AuthException() {
        super();
    }
}
