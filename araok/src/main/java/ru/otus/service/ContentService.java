package ru.otus.service;


import ru.otus.constant.TypeContent;
import ru.otus.dto.ContentDto;
import ru.otus.dto.ContentWithContentMediaAndMediaSubtitleDto;

import java.util.List;

public interface ContentService {

    List<ContentDto> findContentsByType(TypeContent type);

    List<ContentDto> findContentsByName(String name);

    ContentDto findContentById(Long id);

    ContentDto save(ContentWithContentMediaAndMediaSubtitleDto content);
}
