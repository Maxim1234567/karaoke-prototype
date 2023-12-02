package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.Content;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentDto {
    private Long id;

    private String name;

    private AgeLimitDto limit;

    private String artist;

    private UserDto user;

    private LocalDate createDate;

    private LanguageDto language;

    public static Content toDomainObject(ContentDto dto) {
        return Content.builder()
                .id(dto.id)
                .name(dto.name)
                .limit(
                        AgeLimitDto.toDomainObject(dto.limit)
                )
                .artist(dto.artist)
                .user(
                        UserDto.toDomainObject(dto.user)
                )
                .createDate(dto.createDate)
                .language(
                        LanguageDto.toDomainObject(dto.language)
                )
                .build();
    }

    public static ContentDto toDto(Content content) {
        return ContentDto.builder()
                .id(content.getId())
                .name(content.getName())
                .limit(
                        AgeLimitDto.toDto(content.getLimit())
                )
                .artist(content.getArtist())
                .user(
                        UserDto.toDto(content.getUser())
                )
                .createDate(content.getCreateDate())
                .language(
                        LanguageDto.toDto(content.getLanguage())
                )
                .build();
    }
}
