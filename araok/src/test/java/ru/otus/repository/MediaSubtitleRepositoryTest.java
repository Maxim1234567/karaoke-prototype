package ru.otus.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.domain.AgeLimit;
import ru.otus.domain.Content;
import ru.otus.domain.Language;
import ru.otus.domain.MediaSubtitle;
import ru.otus.domain.Subtitle;
import ru.otus.domain.User;
import ru.otus.enums.RoleEnum;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MediaSubtitleRepositoryTest {

    private Long CONTENT_ID;

    @Autowired
    private MediaSubtitleRepository mediaSubtitleRepository;

    @Autowired
    private ContentRepository contentRepository;

    private MediaSubtitle ruSubtitle;

    private MediaSubtitle enSubtitle;

    @BeforeEach
    public void setUp() {
        AgeLimit limit = AgeLimit.builder()
                .id(1L)
                .description("for children under 6 years of age")
                .limit(0L)
                .build();

        User user = User.builder()
                .id(1000000000L)
                .name("Maxim")
                .phone("9993338951")
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

        contentRepository.save(content);

        CONTENT_ID = content.getId();

//-------------------------------------ru----------------------------------------
        Subtitle ruSubtitle1 = Subtitle.builder()
                .line("line1")
                .to(1L)
                .from(2L)
                .build();

        Subtitle ruSubtitle2 = Subtitle.builder()
                .line("line2")
                .to(2L)
                .from(3L)
                .build();

        Subtitle ruSubtitle3 = Subtitle.builder()
                .line("line2")
                .to(3L)
                .from(4L)
                .build();

        Language ruLanguage = Language.builder()
                .id(1L)
                .language("Russian")
                .code2("RU")
                .build();

        ruSubtitle = MediaSubtitle.builder()
                .content(content)
                .language(ruLanguage)
                .subtitles(
                        List.of(ruSubtitle1, ruSubtitle2, ruSubtitle3)
                )
                .build();

//-------------------------------------en----------------------------------------

        Subtitle enSubtitle1 = Subtitle.builder()
                .line("line4")
                .to(1L)
                .from(2L)
                .build();

        Subtitle enSubtitle2 = Subtitle.builder()
                .line("line5")
                .to(2L)
                .from(3L)
                .build();

        Subtitle enSubtitle3 = Subtitle.builder()
                .line("line6")
                .to(3L)
                .from(4L)
                .build();

        Language enLanguage = Language.builder()
                .id(2L)
                .language("English")
                .code2("EN")
                .build();

        enSubtitle = MediaSubtitle.builder()
                .content(content)
                .language(enLanguage)
                .subtitles(
                        List.of(enSubtitle1, enSubtitle2, enSubtitle3)
                )
                .build();
    }

    @Test
    public void shouldCorrectSaveMediaSubtitle() {
        MediaSubtitle mediaSubtitle = mediaSubtitleRepository.save(ruSubtitle);

        MediaSubtitle result = mediaSubtitleRepository.findById(mediaSubtitle.getId()).get();

        assertThatMediaSubtitle(ruSubtitle, result);
    }

    @Test
    public void shouldCorrectReturnMediaSubtitleByMediaSubtitleId() {
        mediaSubtitleRepository.save(ruSubtitle);
        MediaSubtitle enMediaSubtitle = mediaSubtitleRepository.save(enSubtitle);


        MediaSubtitle result = mediaSubtitleRepository.findById(enMediaSubtitle.getId()).get();

        assertThatMediaSubtitle(enSubtitle, result);
    }

    @Test
    public void shouldCorrectReturnMediaSubtitleByContentId() {
        mediaSubtitleRepository.save(ruSubtitle);
        mediaSubtitleRepository.save(enSubtitle);

        List<MediaSubtitle> all = mediaSubtitleRepository.findAll();

        List<MediaSubtitle> mediaSubtitles = mediaSubtitleRepository.findByContentId(CONTENT_ID);

        assertThat(mediaSubtitles).isNotNull().hasSize(2);

        MediaSubtitle ruMediaSubtitle = mediaSubtitles.stream()
                .filter(m -> m.getLanguage().getId().equals(1L))
                .findFirst()
                .get();

        MediaSubtitle enMediaSubtitle = mediaSubtitles.stream()
                .filter(m -> m.getLanguage().getId().equals(2L))
                .findFirst()
                .get();

        assertThatMediaSubtitle(ruSubtitle, ruMediaSubtitle);
        assertThatMediaSubtitle(enSubtitle, enMediaSubtitle);
    }

    @Test
    public void shouldCorrectReturnMediaSubtitleByLanguageId() {
        mediaSubtitleRepository.save(ruSubtitle);
        mediaSubtitleRepository.save(enSubtitle);

        MediaSubtitle result = mediaSubtitleRepository.findByLanguageId(
                ruSubtitle.getLanguage().getId()
        ).get(0);

        assertThatMediaSubtitle(ruSubtitle, result);
    }

    @Test
    public void shouldCorrectReturnMediaSubtitleByContentIdAndLanguageId() {
        mediaSubtitleRepository.save(ruSubtitle);
        mediaSubtitleRepository.save(enSubtitle);

        MediaSubtitle result = mediaSubtitleRepository.findByContentIdAndLanguageId(
                ruSubtitle.getContent().getId(), ruSubtitle.getLanguage().getId()
        ).get();

        assertThatMediaSubtitle(ruSubtitle, result);
    }

    @Test
    public void shouldCorrectReturnAllLanguageSubtitleByContentId() {
        mediaSubtitleRepository.save(ruSubtitle);
        mediaSubtitleRepository.save(enSubtitle);

        List<Language> result = mediaSubtitleRepository.findAllLanguageSubtitleByContentId(CONTENT_ID);

        assertEqualsLanguageList(
                List.of(
                        ruSubtitle.getLanguage(),
                        enSubtitle.getLanguage()
                ),
                result
        );
    }

    private void assertEqualsLanguageList(List<Language> excepted, List<Language> result) {
        assertThat(result).isNotNull()
                .hasSize(excepted.size());

        for (int i = 0; i < excepted.size(); i++) {
            assertEqualsLanguage(excepted.get(i), result.get(i));
        }
    }

    private void assertEqualsLanguage(Language excepted, Language result) {
        assertThat(result).isNotNull()
                .matches(l -> l.getId().equals(excepted.getId()))
                .matches(l -> l.getCode2().equals(excepted.getCode2()))
                .matches(l -> l.getLanguage().equals(excepted.getLanguage()));
    }

    private void assertThatMediaSubtitle(MediaSubtitle expected, MediaSubtitle result) {
        Language language = result.getLanguage();
        List<Subtitle> subtitles = result.getSubtitles();

        assertThat(language).isNotNull()
                .matches(l -> l.getId().equals(expected.getLanguage().getId()))
                .matches(l -> l.getCode2().equals(expected.getLanguage().getCode2()))
                .matches(l -> l.getLanguage().equals(expected.getLanguage().getLanguage()));

        assertThatSubtitleList(expected.getSubtitles(), subtitles);

        assertThat(result).isNotNull();
    }

    private void assertThatSubtitleList(List<Subtitle> expected, List<Subtitle> result) {
        assertThat(result).isNotNull()
                .hasSize(expected.size());

        for(int i = 0; i < expected.size(); i++) {
            assertThatSubtitle(expected.get(i), result.get(i));
        }
    }

    private void assertThatSubtitle(Subtitle expected, Subtitle result) {
        assertThat(result).isNotNull()
                .matches(s -> s.getId().equals(expected.getId()))
                .matches(s -> s.getLine().equals(expected.getLine()))
                .matches(s -> s.getTo().equals(expected.getTo()))
                .matches(s -> s.getFrom().equals(expected.getFrom()));
    }
}
