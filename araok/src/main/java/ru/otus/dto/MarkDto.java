package ru.otus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.domain.Mark;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkDto {
    private Long id;

    private Long start;

    private Long end;

    private Long repeat;

    private Long delay;

    public static Mark toDomainObject(MarkDto dto) {
        return Mark.builder()
                .id(dto.id)
                .start(dto.start)
                .end(dto.end)
                .repeat(dto.repeat)
                .delay(dto.delay)
                .build();
    }

    public static MarkDto toDto(Mark mark) {
        return MarkDto.builder()
                .id(mark.getId())
                .start(mark.getStart())
                .end(mark.getEnd())
                .repeat(mark.getRepeat())
                .delay(mark.getDelay())
                .build();
    }
}