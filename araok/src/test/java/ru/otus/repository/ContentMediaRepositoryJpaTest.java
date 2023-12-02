package ru.otus.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.domain.AgeLimit;
import ru.otus.domain.Content;
import ru.otus.domain.ContentMedia;
import ru.otus.domain.ContentMediaId;
import ru.otus.domain.Language;
import ru.otus.domain.MediaType;
import ru.otus.domain.User;
import ru.otus.enums.RoleEnum;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static ru.otus.Utils.assertEqualsContent;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ContentMediaRepositoryJpaTest {

    @Autowired
    private ContentMediaRepository contentMediaRepository;

    @Autowired
    private ContentRepository contentRepository;

    private byte[] image = {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
    };

    private byte[] video = {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0,
            1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1
    };

    private ContentMedia contentMedia;

    private ContentMedia videoContentMedia;

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

        MediaType imageType = MediaType.builder()
                .id(3L)
                .type("IMAGE")
                .build();

        ContentMediaId contentMediaId = ContentMediaId.builder()
                .content(content)
                .mediaType(imageType)
                .build();

        contentMedia = ContentMedia.builder()
                .contentMediaId(contentMediaId)
                .media(image)
                .build();

        MediaType videoType = MediaType.builder()
                .id(1L)
                .type("VIDEO")
                .build();

        ContentMediaId videoContentMediaId = ContentMediaId.builder()
                .content(content)
                .mediaType(videoType)
                .build();

        videoContentMedia = ContentMedia.builder()
                .contentMediaId(videoContentMediaId)
                .media(video)
                .build();
    }

    @Test
    public void shouldCorrectlySaveContentMedia() {
        contentMediaRepository.save(contentMedia);

        ContentMediaId primaryKey = ContentMediaId.builder()
                .content(contentMedia.getContentMediaId().getContent())
                .mediaType(contentMedia.getContentMediaId().getMediaType())
                .build();

        ContentMedia result = contentMediaRepository.findByContentMediaId(
                primaryKey
        ).orElseThrow();

        MediaType mediaType = contentMedia.getContentMediaId().getMediaType();

        assertEqualsContent(contentMedia.getContentMediaId().getContent(), result.getContentMediaId().getContent());

        assertThat(result).isNotNull()
                .matches(r -> r.getContentMediaId().getMediaType().getId().equals(contentMedia.getContentMediaId().getMediaType().getId()));

        assertThat(result.getContentMediaId().getMediaType()).isNotNull()
                .matches(m -> m.getId().equals(mediaType.getId()))
                .matches(m -> m.getType().equals(mediaType.getType()));

        assertArrayEquals(result.getMedia(), contentMedia.getMedia());
    }

    @Test
    public void shouldCorrectlyFindContentMediaByContentMediaId() {
        contentMediaRepository.save(contentMedia);

        ContentMediaId primaryKey = ContentMediaId.builder()
                .content(contentMedia.getContentMediaId().getContent())
                .mediaType(contentMedia.getContentMediaId().getMediaType())
                .build();

        ContentMedia result = contentMediaRepository.findByContentMediaId(
                primaryKey
        ).orElseThrow();

        MediaType mediaType = contentMedia.getContentMediaId().getMediaType();

        assertEqualsContent(contentMedia.getContentMediaId().getContent(), result.getContentMediaId().getContent());

        assertThat(result).isNotNull()
                .matches(r -> r.getContentMediaId().getMediaType().getId().equals(contentMedia.getContentMediaId().getMediaType().getId()));

        assertThat(result.getContentMediaId().getMediaType()).isNotNull()
                .matches(m -> m.getId().equals(mediaType.getId()))
                .matches(m -> m.getType().equals(mediaType.getType()));

        assertArrayEquals(result.getMedia(), contentMedia.getMedia());
    }

    @Test
    public void shouldCorrectlyFindContentMediaByTypeId() {
        contentMediaRepository.save(contentMedia);
        contentMediaRepository.save(videoContentMedia);

        long typeIdImage = contentMedia.getContentMediaId().getMediaType().getId();
        long typeIdVideo = videoContentMedia.getContentMediaId().getMediaType().getId();

        List<ContentMedia> resultImage = contentMediaRepository.findByTypeId(typeIdImage);
        List<ContentMedia> resultVideo = contentMediaRepository.findByTypeId(typeIdVideo);

        MediaType mediaTypeImage = contentMedia.getContentMediaId().getMediaType();
        MediaType mediaTypeVideo = videoContentMedia.getContentMediaId().getMediaType();

        ContentMedia contentMediaImage = resultImage.get(0);
        ContentMedia contentMediaVideo = resultVideo.get(0);

        assertEqualsContent(contentMedia.getContentMediaId().getContent(), contentMediaImage.getContentMediaId().getContent());

        assertThat(resultImage).hasSize(1).isNotNull()
                .allMatch(r -> r.getContentMediaId().getMediaType().getId().equals(contentMedia.getContentMediaId().getMediaType().getId()));

        assertEqualsContent(contentMediaVideo.getContentMediaId().getContent(), contentMediaVideo.getContentMediaId().getContent());

        assertThat(resultVideo).hasSize(1).isNotNull()
                .allMatch(r -> r.getContentMediaId().getMediaType().getId().equals(contentMediaVideo.getContentMediaId().getMediaType().getId()));

        assertThat(contentMediaImage.getContentMediaId().getMediaType()).isNotNull()
                .matches(m -> m.getId().equals(mediaTypeImage.getId()))
                .matches(m -> m.getType().equals(mediaTypeImage.getType()));

        assertThat(contentMediaVideo.getContentMediaId().getMediaType()).isNotNull()
                .matches(m -> m.getId().equals(mediaTypeVideo.getId()))
                .matches(m -> m.getType().equals(mediaTypeVideo.getType()));

        assertArrayEquals(contentMediaImage.getMedia(), contentMedia.getMedia());
        assertArrayEquals(contentMediaVideo.getMedia(), videoContentMedia.getMedia());
    }

    @Test
    public void shouldCorrectReturnMediaByContentMediaId() {
        contentMediaRepository.save(contentMedia);
        contentMediaRepository.save(videoContentMedia);

        byte[] media = contentMediaRepository.findMediaByContentMediaId(videoContentMedia.getContentMediaId());

        assertArrayEquals(media, videoContentMedia.getMedia());
    }
}
