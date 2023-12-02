package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.ContentType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentTypeDto {
    private ContentTypeIdDto contentTypeId;

    public static ContentType toDomainObject(ContentTypeDto dto) {
        return ContentType.builder()
                .contentTypeId(
                        ContentTypeIdDto.toDomainObject(dto.contentTypeId)
                )
                .build();
    }

    public static ContentTypeDto toDto(ContentType contentType) {
        return ContentTypeDto.builder()
                .contentTypeId(
                        ContentTypeIdDto.toDto(contentType.getContentTypeId())
                )
                .build();
    }
}
