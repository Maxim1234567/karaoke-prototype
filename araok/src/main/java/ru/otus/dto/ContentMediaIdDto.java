package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.ContentMediaId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentMediaIdDto {
    private ContentDto content;

    private MediaTypeDto mediaType;

    public static ContentMediaId toDomainObject(ContentMediaIdDto dto) {
        return ContentMediaId.builder()
                .content(
                        ContentDto.toDomainObject(dto.content)
                )
                .mediaType(
                        MediaTypeDto.toDomainObject(dto.mediaType)
                )
                .build();
    }

    public static ContentMediaIdDto toDto(ContentMediaId contentMediaId) {
        return ContentMediaIdDto.builder()
                .content(
                        ContentDto.toDto(contentMediaId.getContent())
                )
                .mediaType(
                        MediaTypeDto.toDto(contentMediaId.getMediaType())
                )
                .build();
    }
}
