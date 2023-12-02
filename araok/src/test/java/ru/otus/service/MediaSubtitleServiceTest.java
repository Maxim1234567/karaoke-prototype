package ru.otus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.domain.AgeLimit;
import ru.otus.domain.Content;
import ru.otus.domain.Language;
import ru.otus.domain.MediaSubtitle;
import ru.otus.domain.Subtitle;
import ru.otus.domain.User;
import ru.otus.dto.LanguageDto;
import ru.otus.dto.MediaSubtitleDto;
import ru.otus.dto.SubtitleDto;
import ru.otus.enums.RoleEnum;
import ru.otus.exception.NotFoundContentException;
import ru.otus.repository.MediaSubtitleRepository;
import ru.otus.service.impl.MediaSubtitleServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.otus.Utils.assertEqualsContentDto;

@ExtendWith(MockitoExtension.class)
public class MediaSubtitleServiceTest {

    @Mock
    private MediaSubtitleRepository mediaSubtitleRepository;

    private MediaSubtitleService mediaSubtitleService;

    private MediaSubtitle mediaSubtitle;

    private List<Language> languages;

    @BeforeEach
    public void setUp() {
        mediaSubtitleService = new MediaSubtitleServiceImpl(mediaSubtitleRepository);

        Subtitle subtitle1 = Subtitle.builder()
                .id(1L)
                .line("line1")
                .to(1L)
                .from(2L)
                .build();

        Subtitle subtitle2 = Subtitle.builder()
                .id(2L)
                .line("line2")
                .to(2L)
                .from(3L)
                .build();

        Subtitle subtitle3 = Subtitle.builder()
                .id(3L)
                .line("line3")
                .to(3L)
                .from(4L)
                .build();

        AgeLimit limit = AgeLimit.builder()
                .id(1L)
                .description("for children under 6 years of age")
                .limit(0L)
                .build();

        User user = User.builder()
                .id(1L)
                .name("Maxim")
                .phone("89993338951")
                .password("12345")
                .birthDate(LocalDate.of(1994, 8, 5))
                .role(RoleEnum.USER)
                .build();

        Language language = Language.builder()
                .id(1L)
                .code2("RU")
                .language("Russian")
                .build();

        Content content = Content.builder()
                .id(1L)
                .name("Unknown Content")
                .limit(limit)
                .artist("Unknown Artist")
                .user(user)
                .createDate(LocalDate.now())
                .language(language)
                .build();

        mediaSubtitle = MediaSubtitle.builder()
                .id(1L)
                .content(content)
                .language(language)
                .subtitles(
                        List.of(subtitle1, subtitle2, subtitle3)
                )
                .build();

        languages = List.of(
                Language.builder()
                        .id(1L)
                        .code2("RU")
                        .language("Russian")
                        .build(),
                Language.builder()
                        .id(2L)
                        .code2("EN")
                        .language("English")
                        .build(),
                Language.builder()
                        .id(3L)
                        .code2("DE")
                        .language("German")
                        .build()
        );
    }

    @Test
    public void shouldCorrectReturnMediaSubtitleByContentIdAndLanguageId() {
        given(mediaSubtitleRepository.findByContentIdAndLanguageId(
                eq(mediaSubtitle.getContent().getId()),
                eq(mediaSubtitle.getLanguage().getId()))
        ).willReturn(Optional.of(mediaSubtitle));

        MediaSubtitleDto mediaSubtitleDto = mediaSubtitleService.findMediaSubtitleByContentIdAndLanguageId(
                mediaSubtitle.getContent().getId(), mediaSubtitle.getLanguage().getId()
        );

        verify(mediaSubtitleRepository, times(1)).findByContentIdAndLanguageId(
                eq(mediaSubtitle.getContent().getId()), eq(mediaSubtitle.getLanguage().getId())
        );

        assertThat(mediaSubtitleDto).isNotNull()
                .matches(ms -> ms.getId().equals(mediaSubtitle.getId()));

        assertEqualsContentDto(mediaSubtitle.getContent(), mediaSubtitleDto.getContent());

        assertThat(mediaSubtitleDto.getLanguage()).isNotNull()
                .matches(l -> l.getId().equals(mediaSubtitle.getLanguage().getId()))
                .matches(l -> l.getCode2().equals(mediaSubtitle.getLanguage().getCode2()))
                .matches(l -> l.getLanguage().equals(mediaSubtitle.getLanguage().getLanguage()));

        assertEqualsSubtitleList(mediaSubtitle.getSubtitles(), mediaSubtitleDto.getSubtitles());
    }

    @Test
    public void shouldDoesThrowNotFoundContentException() {
        given(mediaSubtitleRepository.findByContentIdAndLanguageId(
                eq(mediaSubtitle.getContent().getId()),
                eq(mediaSubtitle.getLanguage().getId())
        )).willReturn(Optional.empty());

        assertThrows(NotFoundContentException.class,
                () -> mediaSubtitleService.findMediaSubtitleByContentIdAndLanguageId(
                        mediaSubtitle.getContent().getId(),
                        mediaSubtitle.getLanguage().getId()
                )
        );
    }

    @Test
    public void shouldCorrectSaveMediaSubtitle() {
        given(mediaSubtitleRepository.save((any(MediaSubtitle.class))))
                .willReturn(mediaSubtitle);

        MediaSubtitleDto mediaSubtitleDto = MediaSubtitleDto.toDto(mediaSubtitle);
        MediaSubtitleDto result = mediaSubtitleService.save(mediaSubtitleDto);

        verify(mediaSubtitleRepository, times(1)).save(any(MediaSubtitle.class));

        assertThat(result).isNotNull()
                .matches(ms -> ms.getId().equals(mediaSubtitle.getId()));

        assertEqualsContentDto(mediaSubtitle.getContent(), mediaSubtitleDto.getContent());

        assertThat(result.getLanguage()).isNotNull()
                .matches(l -> l.getId().equals(mediaSubtitle.getLanguage().getId()))
                .matches(l -> l.getCode2().equals(mediaSubtitle.getLanguage().getCode2()))
                .matches(l -> l.getLanguage().equals(mediaSubtitle.getLanguage().getLanguage()));

        assertEqualsSubtitleList(mediaSubtitle.getSubtitles(), result.getSubtitles());
    }

    @Test
    public void shouldCorrectReturnAllLanguageByContentId() {
        given(mediaSubtitleRepository.findAllLanguageSubtitleByContentId(eq(1L)))
                .willReturn(languages);

        List<LanguageDto> result = mediaSubtitleService.findAllLanguageSubtitleByContentId(1L);

        verify(mediaSubtitleRepository, times(1)).findAllLanguageSubtitleByContentId(eq(1L));

        assertEqualsLanguageList(languages, result);
    }

    private void assertEqualsSubtitleList(List<Subtitle> excepted, List<SubtitleDto> result) {
        assertThat(result).isNotNull()
                .hasSize(excepted.size());

        for(int i = 0; i < excepted.size(); i++) {
            assertEqualsSubtitle(excepted.get(i), result.get(i));
        }
    }

    private void assertEqualsSubtitle(Subtitle excepted, SubtitleDto result) {
        assertThat(result).isNotNull()
                .matches(s -> s.getId().equals(excepted.getId()))
                .matches(s -> s.getLine().equals(excepted.getLine()))
                .matches(s -> s.getFrom().equals(excepted.getFrom()))
                .matches(s -> s.getTo().equals(excepted.getTo()));
    }

    private void assertEqualsLanguageList(List<Language> excepted, List<LanguageDto> result) {
        assertThat(result).isNotNull()
                .hasSize(excepted.size());

        for (int i = 0; i < excepted.size(); i++) {
            assertEqualsLanguage(excepted.get(i), result.get(i));
        }
    }

    private void assertEqualsLanguage(Language excepted, LanguageDto result) {
        assertThat(result).isNotNull()
                .matches(l -> l.getId().equals(excepted.getId()))
                .matches(l -> l.getCode2().equals(excepted.getCode2()))
                .matches(l -> l.getLanguage().equals(excepted.getLanguage()));
    }
}
