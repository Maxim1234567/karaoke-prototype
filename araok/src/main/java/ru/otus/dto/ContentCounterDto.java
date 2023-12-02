package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.ContentCounter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentCounterDto {
    private Long id;

    private ContentDto content;

    private UserDto user;

    private Long count;

    public static ContentCounter toDomainObject(ContentCounterDto dto) {
        return ContentCounter.builder()
                .id(dto.id)
                .content(
                        ContentDto.toDomainObject(dto.content)
                )
                .user(
                        UserDto.toDomainObject(dto.user)
                )
                .count(dto.count)
                .build();
    }

    public static ContentCounterDto toDto(ContentCounter contentCounter) {
        return ContentCounterDto.builder()
                .id(contentCounter.getId())
                .content(
                        ContentDto.toDto(contentCounter.getContent())
                )
                .user(
                        UserDto.toDto(contentCounter.getUser())
                )
                .count(contentCounter.getCount())
                .build();
    }
}
