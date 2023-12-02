package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.MediaType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaTypeDto {
    private Long id;

    private String type;

    public static MediaType toDomainObject(MediaTypeDto dto) {
        return MediaType.builder()
                .id(dto.id)
                .type(dto.type)
                .build();
    }

    public static MediaTypeDto toDto(MediaType mediaType) {
        return MediaTypeDto.builder()
                .id(mediaType.getId())
                .type(mediaType.getType())
                .build();
    }
}
