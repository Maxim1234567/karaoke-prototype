package ru.otus.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.service.DateService;

import java.time.LocalDateTime;

@Service
public class DateServiceImpl implements DateService {
    @Override
    public LocalDateTime getDateNow() {
        return LocalDateTime.now();
    }
}
