package ru.otus.exception;

public class NotFoundMediaTypeException extends RuntimeException {
    public NotFoundMediaTypeException() {
        super();
    }

    public NotFoundMediaTypeException(Exception e) {
        super(e);
    }
}
