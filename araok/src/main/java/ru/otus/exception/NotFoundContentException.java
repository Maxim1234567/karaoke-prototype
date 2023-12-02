package ru.otus.exception;

public class NotFoundContentException extends RuntimeException {
    public NotFoundContentException() {
        super();
    }

    public NotFoundContentException(Exception e) {
        super(e);
    }
}
