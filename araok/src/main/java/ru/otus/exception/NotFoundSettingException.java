package ru.otus.exception;

public class NotFoundSettingException extends RuntimeException {
    public NotFoundSettingException() {
        super();
    }

    public NotFoundSettingException(Exception e) {
        super(e);
    }
}
