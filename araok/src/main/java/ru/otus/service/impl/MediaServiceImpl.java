package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.ContentDto;
import ru.otus.dto.ContentMediaDto;
import ru.otus.dto.ContentMediaIdDto;
import ru.otus.dto.MediaTypeDto;
import ru.otus.service.ContentMediaService;
import ru.otus.service.ContentService;
import ru.otus.service.MediaService;
import ru.otus.service.MediaTypeService;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
    private final ContentMediaService contentMediaService;

    private final ContentService contentService;

    private final MediaTypeService mediaTypeService;

    @Override
    @Transactional(readOnly = true)
    public byte[] findMediaByContentIdAndTypeId(Long contentId, Long typeId) {
        log.info("find media by content id: {} and type id: {}", contentId, typeId);

        ContentDto content = contentService.findContentById(contentId);
        MediaTypeDto mediaType = mediaTypeService.findById(typeId);

        ContentMediaIdDto contentMediaId = ContentMediaIdDto.builder()
                .mediaType(mediaType)
                .content(content)
                .build();

        return contentMediaService.findMediaByContentMediaId(contentMediaId);
    }

    @Override
    @Transactional
    public ContentMediaDto save(ContentMediaDto contentMediaDto) {
        log.info("save content media");

        return contentMediaService.save(contentMediaDto);
    }
}
