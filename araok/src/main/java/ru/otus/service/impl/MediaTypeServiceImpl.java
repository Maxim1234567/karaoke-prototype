package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.MediaTypeDto;
import ru.otus.exception.NotFoundMediaTypeException;
import ru.otus.repository.MediaTypeRepository;
import ru.otus.service.MediaTypeService;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaTypeServiceImpl implements MediaTypeService {
    private final MediaTypeRepository mediaTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public MediaTypeDto findById(Long id) {
        log.info("find media type by id: {}", id);

        return mediaTypeRepository
                .findById(id)
                .map(MediaTypeDto::toDto)
                .orElseThrow(NotFoundMediaTypeException::new);
    }
}
