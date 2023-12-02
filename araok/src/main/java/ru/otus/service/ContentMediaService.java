package ru.otus.service;

import ru.otus.dto.ContentMediaDto;
import ru.otus.dto.ContentMediaIdDto;

public interface ContentMediaService {
    ContentMediaDto findContentMediaById(ContentMediaIdDto contentMediaIdDto);

    byte[] findMediaByContentMediaId(ContentMediaIdDto contentMediaId);

    ContentMediaDto save(ContentMediaDto contentMediaDto);
}
