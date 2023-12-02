package ru.otus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.dto.AgeLimitDto;
import ru.otus.dto.ContentDto;
import ru.otus.dto.LanguageDto;
import ru.otus.dto.MediaSubtitleDto;
import ru.otus.dto.SubtitleDto;
import ru.otus.dto.UserDto;
import ru.otus.enums.RoleEnum;
import ru.otus.security.filter.JwtFilter;
import ru.otus.service.MediaSubtitleService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MediaSubtitleController.class)
public class MediaSubtitleControllerTest {

    private static final String JSON_LANGUAGE =
            """
                            [
                                {
                                    "id": 1,
                                    "language": "Russian",
                                    "code2": "RU"
                                },
                                {
                                    "id": 2,
                                    "language": "English",
                                    "code2": "EN"
                                },
                                {
                                    "id": 3,
                                    "language": "German",
                                    "code2": "DE"
                                }
                            ]
                    """;

    private static final String JSON_MEDIA_SUBTITLE =
            """
                            {
                                "id": 1,
                                "content": {
                                    "id": 1,
                                    "name": "Unknown Content",
                                    "limit": {
                                        "id": 1,
                                        "description": "for children under 6 years of age",
                                        "limit": 0
                                    },
                                    "artist": "Unknown Artist",
                                    "user": {
                                        "id": 1,
                                        "name": "Maxim",
                                        "phone": "89993338951",
                                        "password": "12345",
                                        "birthDate": "1994-08-05",
                                        "role": "USER"
                                    },
                                    "createDate": "2023-10-18",
                                    "language": {
                                        "id": 1,
                                        "language": "Russian",
                                        "code2": "RU"
                                    }
                                },
                                "language": {
                                    "id": 1,
                                    "language": "Russian",
                                    "code2": "RU"
                                },
                                "subtitles": [
                                    {
                                        "id": 1,
                                        "line": "line1",
                                        "to": 1,
                                        "from": 2
                                    },
                                    {
                                        "id": 2,
                                        "line": "line2",
                                        "to": 2,
                                        "from": 3
                                    },
                                    {
                                        "id": 3,
                                        "line": "line2",
                                        "to": 3,
                                        "from": 4
                                    }
                                ]
                            }
                    """;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MediaSubtitleService mediaSubtitleService;

    @MockBean
    private JwtFilter jwtFilter;

    private MediaSubtitleDto mediaSubtitle;

    private List<LanguageDto> languages;

    @BeforeEach
    public void setUp() {
        AgeLimitDto limit = AgeLimitDto.builder()
                .id(1L)
                .description("for children under 6 years of age")
                .limit(0L)
                .build();

        UserDto user = UserDto.builder()
                .id(1L)
                .name("Maxim")
                .phone("89993338951")
                .password("12345")
                .birthDate(LocalDate.of(1994, 8, 5))
                .role(RoleEnum.USER)
                .build();

        LanguageDto language = LanguageDto.builder()
                .id(1L)
                .code2("RU")
                .language("Russian")
                .build();

        ContentDto content = ContentDto.builder()
                .id(1L)
                .name("Unknown Content")
                .limit(limit)
                .artist("Unknown Artist")
                .user(user)
                .createDate(LocalDate.of(2023, 10, 18))
                .language(language)
                .build();

        SubtitleDto subtitle1 = SubtitleDto.builder()
                .id(1L)
                .line("line1")
                .to(1L)
                .from(2L)
                .build();

        SubtitleDto subtitle2 = SubtitleDto.builder()
                .id(2L)
                .line("line2")
                .to(2L)
                .from(3L)
                .build();

        SubtitleDto subtitle3 = SubtitleDto.builder()
                .id(3L)
                .line("line2")
                .to(3L)
                .from(4L)
                .build();

        mediaSubtitle = MediaSubtitleDto.builder()
                .id(1L)
                .language(language)
                .subtitles(
                        List.of(subtitle1, subtitle2, subtitle3)
                )
                .content(content)
                .build();

        languages = List.of(
                LanguageDto.builder()
                        .id(1L)
                        .code2("RU")
                        .language("Russian")
                        .build(),
                LanguageDto.builder()
                        .id(2L)
                        .code2("EN")
                        .language("English")
                        .build(),
                LanguageDto.builder()
                        .id(3L)
                        .code2("DE")
                        .language("German")
                        .build()
        );

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldCorrectReturnSubtitleById() throws Exception {
        given(mediaSubtitleService.findMediaSubtitleByContentIdAndLanguageId(eq(mediaSubtitle.getContent().getId()), eq(mediaSubtitle.getLanguage().getId())))
                .willReturn(mediaSubtitle);

        mvc.perform(get("/api/subtitle/" + mediaSubtitle.getContent().getId() + "/" + mediaSubtitle.getLanguage().getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(JSON_MEDIA_SUBTITLE));

        verify(mediaSubtitleService, times(1))
                .findMediaSubtitleByContentIdAndLanguageId(mediaSubtitle.getContent().getId(), mediaSubtitle.getLanguage().getId());
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldCorrectReturnAllLanguageByContentId() throws Exception {
        given(mediaSubtitleService.findAllLanguageSubtitleByContentId(1L))
                .willReturn(languages);

        mvc.perform(get("/api/subtitle/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(JSON_LANGUAGE));

        verify(mediaSubtitleService, times(1))
                .findAllLanguageSubtitleByContentId(1L);
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldCorrectSaveSubtitle() throws Exception {
        given(mediaSubtitleService.save(any(MediaSubtitleDto.class)))
                .willReturn(mediaSubtitle);

        mvc.perform(post("/api/subtitle").with(csrf())
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .content(mapper.writeValueAsString(mediaSubtitle))
        ).andExpect(status().isCreated())
        .andExpect(content().json(JSON_MEDIA_SUBTITLE));

        verify(mediaSubtitleService, times(1))
                .save(any(MediaSubtitleDto.class));
    }
}
