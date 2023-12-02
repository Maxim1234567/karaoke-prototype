package ru.otus.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestProperties {
    private final String regexpUrl;

    private final String method;
}
