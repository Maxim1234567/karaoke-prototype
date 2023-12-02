package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.Subtitle;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubtitleDto {
    private Long id;

    private String line;

    private Long to;

    private Long from;

    public static Subtitle toDomainObject(SubtitleDto dto) {
        return Subtitle.builder()
                .id(dto.id)
                .line(dto.line)
                .to(dto.to)
                .from(dto.from)
                .build();
    }

    public static SubtitleDto toDto(Subtitle subtitle) {
        return SubtitleDto.builder()
                .id(subtitle.getId())
                .line(subtitle.getLine())
                .to(subtitle.getTo())
                .from(subtitle.getFrom())
                .build();
    }
}
