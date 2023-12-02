package ru.otus.service;


import ru.otus.dto.MediaTypeDto;

public interface MediaTypeService {
    MediaTypeDto findById(Long id);
}
