package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.ContentRecommended;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentRecommendedDto {
    private Long id;

    private ContentDto content;

    public static ContentRecommended toDomainObject(ContentRecommendedDto dto) {
        return ContentRecommended.builder()
                .id(dto.id)
                .content(
                        ContentDto.toDomainObject(dto.content)
                )
                .build();
    }

    public static ContentRecommendedDto toDto(ContentRecommended contentRecommended) {
        return ContentRecommendedDto.builder()
                .id(contentRecommended.getId())
                .content(
                        ContentDto.toDto(contentRecommended.getContent())
                )
                .build();
    }
}
