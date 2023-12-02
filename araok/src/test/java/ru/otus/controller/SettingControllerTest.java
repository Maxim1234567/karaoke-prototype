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
import ru.otus.dto.MarkDto;
import ru.otus.dto.SettingDto;
import ru.otus.dto.UserDto;
import ru.otus.enums.RoleEnum;
import ru.otus.security.filter.JwtFilter;
import ru.otus.service.SettingService;

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

@WebMvcTest(SettingController.class)
public class SettingControllerTest {
    private final static String JSON_SETTING =
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
                                "marks": [
                                    {
                                        "id": 1,
                                        "start": 1,
                                        "end": 1,
                                        "repeat": 1,
                                        "delay": 1
                                    },
                                    {
                                        "id": 2,
                                        "start": 2,
                                        "end": 2,
                                        "repeat": 2,
                                        "delay": 2
                                    },
                                    {
                                        "id": 3,
                                        "start": 3,
                                        "end": 3,
                                        "repeat": 3,
                                        "delay": 3
                                    }
                                ]
                            }
                    """;

    @MockBean
    private SettingService settingService;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    private SettingDto setting;

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


        MarkDto mark1 = MarkDto.builder()
                .id(1L)
                .start(1L)
                .end(1L)
                .delay(1L)
                .repeat(1L)
                .build();

        MarkDto mark2 = MarkDto.builder()
                .id(2L)
                .start(2L)
                .end(2L)
                .delay(2L)
                .repeat(2L)
                .build();

        MarkDto mark3 = MarkDto.builder()
                .id(3L)
                .start(3L)
                .end(3L)
                .delay(3L)
                .repeat(3L)
                .build();

        setting = SettingDto.builder()
                .id(1L)
                .content(content)
                .marks(List.of(mark1, mark2, mark3))
                .build();

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldCorrectSaveSetting() throws Exception {
        given(settingService.save(any(SettingDto.class)))
                .willReturn(setting);

        mvc.perform(post("/api/setting").with(csrf())
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .content(mapper.writeValueAsString(setting))
        ).andExpect(status().isCreated())
        .andExpect(content().json(JSON_SETTING));

        verify(settingService, times(1))
                .save(any(SettingDto.class));
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldCorrectReturnSetting() throws Exception {
        given(settingService.findByContentId(eq(setting.getContent().getId())))
                .willReturn(setting);

        mvc.perform(get("/api/setting/1").with(csrf())
                        .header("Accept",  "application/json")
                        .header("Content-Type", "application/json")
        ).andExpect(status().isOk())
        .andExpect(content().json(JSON_SETTING));

        verify(settingService, times(1))
                .findByContentId(eq(setting.getContent().getId()));
    }
}
