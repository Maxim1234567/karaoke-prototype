package ru.otus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.domain.MediaType;
import ru.otus.dto.MediaTypeDto;
import ru.otus.exception.NotFoundMediaTypeException;
import ru.otus.repository.MediaTypeRepository;
import ru.otus.service.impl.MediaTypeServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MediaTypeServiceTest {

    @Mock
    private MediaTypeRepository mediaTypeRepository;

    private MediaTypeService mediaTypeService;

    private MediaType mediaType;

    @BeforeEach
    public void setUp() {
        mediaTypeService = new MediaTypeServiceImpl(
                mediaTypeRepository
        );

        mediaType = MediaType.builder()
                .id(1L)
                .type("VIDEO")
                .build();
    }

    @Test
    public void shouldCorrectReturnMediaType() {
        given(mediaTypeRepository.findById(eq(mediaType.getId())))
                .willReturn(Optional.of(mediaType));

        MediaTypeDto result = mediaTypeService.findById(mediaType.getId());

        verify(mediaTypeRepository, times(1)).findById(eq(mediaType.getId()));

        assertThat(result).isNotNull()
                .matches(m -> m.getId().equals(mediaType.getId()))
                .matches(m -> m.getType().equals(mediaType.getType()));
    }

    @Test
    public void shouldDoesThrowsNotFoundMediaTypeExceptionFindById() {
        given(mediaTypeRepository.findById(eq(mediaType.getId())))
                .willReturn(Optional.empty());

        assertThrows(NotFoundMediaTypeException.class, () -> mediaTypeService.findById(mediaType.getId()));

        verify(mediaTypeRepository, times(1)).findById(eq(mediaType.getId()));
    }
}
