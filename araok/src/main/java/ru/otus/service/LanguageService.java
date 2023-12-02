package ru.otus.service;


import ru.otus.dto.LanguageDto;

import java.util.List;

public interface LanguageService {
    List<LanguageDto> getAll();
}
