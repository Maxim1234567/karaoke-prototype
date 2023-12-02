package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.ContentTypeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentTypeIdDto {
    private ContentDto content;

    private MediaTypeDto mediaType;

    public static ContentTypeId toDomainObject(ContentTypeIdDto dto) {
        return ContentTypeId.builder()
                .content(
                        ContentDto.toDomainObject(dto.content)
                )
                .mediaType(
                        MediaTypeDto.toDomainObject(dto.mediaType)
                )
                .build();
    }

    public static ContentTypeIdDto toDto(ContentTypeId contentTypeId) {
        return ContentTypeIdDto.builder()
                .content(
                        ContentDto.toDto(contentTypeId.getContent())
                )
                .mediaType(
                        MediaTypeDto.toDto(contentTypeId.getMediaType())
                )
                .build();
    }
}
