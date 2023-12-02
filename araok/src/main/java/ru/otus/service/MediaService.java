package ru.otus.service;


import ru.otus.dto.ContentMediaDto;

public interface MediaService {
    byte[] findMediaByContentIdAndTypeId(Long contentId, Long typeId);

    ContentMediaDto save(ContentMediaDto contentMediaDto);
}
