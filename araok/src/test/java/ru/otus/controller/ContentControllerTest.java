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
import ru.otus.constant.TypeContent;
import ru.otus.dto.AgeLimitDto;
import ru.otus.dto.ContentDto;
import ru.otus.dto.ContentWithContentMediaAndMediaSubtitleDto;
import ru.otus.dto.LanguageDto;
import ru.otus.dto.UserDto;
import ru.otus.enums.RoleEnum;
import ru.otus.security.filter.JwtFilter;
import ru.otus.service.ContentService;

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

@WebMvcTest(ContentController.class)
public class ContentControllerTest {

    private static String JSON_CONTENT =
            "{\n" +
                    "    \"id\": 1,\n" +
                    "    \"name\": \"Unknown Content\",\n" +
                    "    \"limit\": {\n" +
                    "        \"id\": 1,\n" +
                    "        \"description\": \"for children under 6 years of age\",\n" +
                    "        \"limit\": 0\n" +
                    "    },\n" +
                    "    \"artist\": \"Unknown Artist\",\n" +
                    "    \"user\": {\n" +
                    "        \"id\": 1,\n" +
                    "        \"name\": \"Maxim\",\n" +
                    "        \"phone\": \"89993338951\",\n" +
                    "        \"password\": \"12345\",\n" +
                    "        \"birthDate\": \"1994-08-05\",\n" +
                    "        \"role\": \"USER\"\n" +
                    "    },\n" +
                    "    \"createDate\": \"2023-10-19\",\n" +
                    "    \"language\": {\n" +
                    "        \"id\": 1,\n" +
                    "        \"language\": \"Russian\",\n" +
                    "        \"code2\": \"RU\"\n" +
                    "    }\n" +
                    "}";

    private static String JSON_CONTENTS =
            "[\n" +
                    "    {\n" +
                    "        \"id\": 1,\n" +
                    "        \"name\": \"Unknown Content\",\n" +
                    "        \"limit\": {\n" +
                    "            \"id\": 1,\n" +
                    "            \"description\": \"for children under 6 years of age\",\n" +
                    "            \"limit\": 0\n" +
                    "        },\n" +
                    "        \"artist\": \"Unknown Artist\",\n" +
                    "        \"user\": {\n" +
                    "            \"id\": 1,\n" +
                    "            \"name\": \"Maxim\",\n" +
                    "            \"phone\": \"89993338951\",\n" +
                    "            \"password\": \"12345\",\n" +
                    "            \"birthDate\": \"1994-08-05\",\n" +
                    "            \"role\": \"USER\"\n" +
                    "        },\n" +
                    "        \"createDate\": \"2023-10-19\",\n" +
                    "        \"language\": {\n" +
                    "            \"id\": 1,\n" +
                    "            \"language\": \"Russian\",\n" +
                    "            \"code2\": \"RU\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\": 2,\n" +
                    "        \"name\": \"Unknown Content 2\",\n" +
                    "        \"limit\": {\n" +
                    "            \"id\": 5,\n" +
                    "            \"description\": \"prohibited for children\",\n" +
                    "            \"limit\": 18\n" +
                    "        },\n" +
                    "        \"artist\": \"Unknown Artist 2\",\n" +
                    "        \"user\": {\n" +
                    "            \"id\": 1,\n" +
                    "            \"name\": \"Maxim\",\n" +
                    "            \"phone\": \"89993338951\",\n" +
                    "            \"password\": \"12345\",\n" +
                    "            \"birthDate\": \"1994-08-05\",\n" +
                    "            \"role\": \"USER\"\n" +
                    "        },\n" +
                    "        \"createDate\": \"2023-10-19\",\n" +
                    "        \"language\": {\n" +
                    "            \"id\": 2,\n" +
                    "            \"language\": \"English\",\n" +
                    "            \"code2\": \"EN\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"id\": 3,\n" +
                    "        \"name\": \"Unknown Content 3\",\n" +
                    "        \"limit\": {\n" +
                    "            \"id\": 3,\n" +
                    "            \"description\": \"for children over 12 years of age\",\n" +
                    "            \"limit\": 12\n" +
                    "        },\n" +
                    "        \"artist\": \"Unknown Artist 3\",\n" +
                    "        \"user\": {\n" +
                    "            \"id\": 1,\n" +
                    "            \"name\": \"Maxim\",\n" +
                    "            \"phone\": \"89993338951\",\n" +
                    "            \"password\": \"12345\",\n" +
                    "            \"birthDate\": \"1994-08-05\",\n" +
                    "            \"role\": \"USER\"\n" +
                    "        },\n" +
                    "        \"createDate\": \"2023-10-19\",\n" +
                    "        \"language\": {\n" +
                    "            \"id\": 3,\n" +
                    "            \"language\": \"German\",\n" +
                    "            \"code2\": \"DE\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "]";

    private static String JSON_CONTENT_WITH_CONTENT_MEDIA_AND_MEDIA_SUBTITLE =
            "{\n" +
                    "    \"content\": {\n" +
                    "        \"id\": 1,\n" +
                    "        \"name\": \"Unknown Content\",\n" +
                    "        \"limit\": {\n" +
                    "            \"id\": 1,\n" +
                    "            \"description\": \"for children under 6 years of age\",\n" +
                    "            \"limit\": 0\n" +
                    "        },\n" +
                    "        \"artist\": \"Unknown Artist\",\n" +
                    "        \"user\": {\n" +
                    "            \"id\": 1,\n" +
                    "            \"name\": \"Maxim\",\n" +
                    "            \"phone\": \"89993338951\",\n" +
                    "            \"password\": \"12345\",\n" +
                    "            \"birthDate\": \"1994-08-05\",\n" +
                    "            \"role\": \"USER\"\n" +
                    "        },\n" +
                    "        \"createDate\": \"2023-10-19\",\n" +
                    "        \"language\": {\n" +
                    "            \"id\": 1,\n" +
                    "            \"language\": \"Russian\",\n" +
                    "            \"code2\": \"RU\"\n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"mediaSubtitle\": {\n" +
                    "        \"id\": 1,\n" +
                    "        \"content\": {\n" +
                    "            \"id\": 1,\n" +
                    "            \"name\": \"Unknown Content\",\n" +
                    "            \"limit\": {\n" +
                    "                \"id\": 1,\n" +
                    "                \"description\": \"for children under 6 years of age\",\n" +
                    "                \"limit\": 0\n" +
                    "            },\n" +
                    "            \"artist\": \"Unknown Artist\",\n" +
                    "            \"user\": {\n" +
                    "                \"id\": 1,\n" +
                    "                \"name\": \"Maxim\",\n" +
                    "                \"phone\": \"89993338951\",\n" +
                    "                \"password\": \"12345\",\n" +
                    "                \"birthDate\": \"1994-08-05\",\n" +
                    "                \"role\": \"USER\"\n" +
                    "            },\n" +
                    "            \"createDate\": \"2023-10-19\",\n" +
                    "            \"language\": {\n" +
                    "                \"id\": 1,\n" +
                    "                \"language\": \"Russian\",\n" +
                    "                \"code2\": \"RU\"\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"language\": {\n" +
                    "            \"id\": 1,\n" +
                    "            \"language\": \"Russian\",\n" +
                    "            \"code2\": \"RU\"\n" +
                    "        },\n" +
                    "        \"subtitles\": [\n" +
                    "            {\n" +
                    "                \"id\": 1,\n" +
                    "                \"line\": \"line1\",\n" +
                    "                \"to\": 1,\n" +
                    "                \"from\": 2\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 2,\n" +
                    "                \"line\": \"line2\",\n" +
                    "                \"to\": 2,\n" +
                    "                \"from\": 3\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"id\": 3,\n" +
                    "                \"line\": \"line3\",\n" +
                    "                \"to\": 3,\n" +
                    "                \"from\": 4\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    \"preview\": {\n" +
                    "        \"contentMediaId\": {\n" +
                    "            \"content\": {\n" +
                    "                \"id\": 1,\n" +
                    "                \"name\": \"Unknown Content\",\n" +
                    "                \"limit\": {\n" +
                    "                    \"id\": 1,\n" +
                    "                    \"description\": \"for children under 6 years of age\",\n" +
                    "                    \"limit\": 0\n" +
                    "                },\n" +
                    "                \"artist\": \"Unknown Artist\",\n" +
                    "                \"user\": {\n" +
                    "                    \"id\": 1,\n" +
                    "                    \"name\": \"Maxim\",\n" +
                    "                    \"phone\": \"89993338951\",\n" +
                    "                    \"password\": \"12345\",\n" +
                    "                    \"birthDate\": \"1994-08-05\",\n" +
                    "                    \"role\": \"USER\"\n" +
                    "                },\n" +
                    "                \"createDate\": \"2023-10-19\",\n" +
                    "                \"language\": {\n" +
                    "                    \"id\": 1,\n" +
                    "                    \"language\": \"Russian\",\n" +
                    "                    \"code2\": \"RU\"\n" +
                    "                }\n" +
                    "            },\n" +
                    "            \"mediaType\": {\n" +
                    "                \"id\": 1,\n" +
                    "                \"type\": \"VIDEO\"\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"media\": \"AQEBAQEBAQEBAQEBAQEBAQAAAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQABAQEBAQEBAQEBAQEBAQEBAAEBAQEBAQEBAQEBAQEBAQEBAQEBAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQABAQEBAQEBAQEBAQEBAQABAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQABAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQAAAAAAAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEAAQEAAAAAAAEBAQEBAQEAAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEAAQABAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEAAQEBAQ==\"\n" +
                    "    },\n" +
                    "    \"video\": {\n" +
                    "        \"contentMediaId\": {\n" +
                    "            \"content\": {\n" +
                    "                \"id\": 1,\n" +
                    "                \"name\": \"Unknown Content\",\n" +
                    "                \"limit\": {\n" +
                    "                    \"id\": 1,\n" +
                    "                    \"description\": \"for children under 6 years of age\",\n" +
                    "                    \"limit\": 0\n" +
                    "                },\n" +
                    "                \"artist\": \"Unknown Artist\",\n" +
                    "                \"user\": {\n" +
                    "                    \"id\": 1,\n" +
                    "                    \"name\": \"Maxim\",\n" +
                    "                    \"phone\": \"89993338951\",\n" +
                    "                    \"password\": \"12345\",\n" +
                    "                    \"birthDate\": \"1994-08-05\",\n" +
                    "                    \"role\": \"USER\"\n" +
                    "                },\n" +
                    "                \"createDate\": \"2023-10-19\",\n" +
                    "                \"language\": {\n" +
                    "                    \"id\": 1,\n" +
                    "                    \"language\": \"Russian\",\n" +
                    "                    \"code2\": \"RU\"\n" +
                    "                }\n" +
                    "            },\n" +
                    "            \"mediaType\": {\n" +
                    "                \"id\": 1,\n" +
                    "                \"type\": \"VIDEO\"\n" +
                    "            }\n" +
                    "        },\n" +
                    "        \"media\": \"AQEBAQEBAQEBAQEBAQEBAQAAAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQABAQEBAQEBAQEBAQEBAQEBAAEBAQEBAQEBAQEBAQEBAQEBAQEBAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQABAQEBAQEBAQEBAQEBAQABAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQABAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQAAAAAAAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEAAQEAAAAAAAEBAQEBAQEAAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEAAQABAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEAAQEBAQ==\"\n" +
                    "    }\n" +
                    "}";

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

    @MockBean
    private ContentService contentService;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    private List<ContentDto> contents;

    @BeforeEach
    public void setUp() {
        AgeLimitDto limit1 = AgeLimitDto.builder()
                .id(1L)
                .description("for children under 6 years of age")
                .limit(0L)
                .build();

        AgeLimitDto limit2 = AgeLimitDto.builder()
                .id(5L)
                .description("prohibited for children")
                .limit(18L)
                .build();

        AgeLimitDto limit3 = AgeLimitDto.builder()
                .id(3L)
                .description("for children over 12 years of age")
                .limit(12L)
                .build();

        LanguageDto language1 = LanguageDto.builder()
                .id(1L)
                .code2("RU")
                .language("Russian")
                .build();

        LanguageDto language2 = LanguageDto.builder()
                .id(2L)
                .code2("EN")
                .language("English")
                .build();

        LanguageDto language3 = LanguageDto.builder()
                .id(3L)
                .code2("DE")
                .language("German")
                .build();

        UserDto user = UserDto.builder()
                .id(1L)
                .name("Maxim")
                .phone("89993338951")
                .password("12345")
                .birthDate(LocalDate.of(1994, 8, 5))
                .role(RoleEnum.USER)
                .build();

        ContentDto content1 = ContentDto.builder()
                .id(1L)
                .name("Unknown Content")
                .limit(limit1)
                .artist("Unknown Artist")
                .user(user)
                .createDate(LocalDate.of(2023, 10, 19))
                .language(language1)
                .build();

        ContentDto content2 = ContentDto.builder()
                .id(2L)
                .name("Unknown Content 2")
                .limit(limit2)
                .artist("Unknown Artist 2")
                .user(user)
                .createDate(LocalDate.of(2023, 10, 19))
                .language(language2)
                .build();

        ContentDto content3 = ContentDto.builder()
                .id(3L)
                .name("Unknown Content 3")
                .limit(limit3)
                .artist("Unknown Artist 3")
                .user(user)
                .createDate(LocalDate.of(2023, 10, 19))
                .language(language3)
                .build();

        contents = List.of(
                content1, content2, content3
        );

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldCorrectReturnAllContents() throws Exception {
        given(contentService.findContentsByType(eq(TypeContent.ALL)))
                .willReturn(contents);

        mvc.perform(get("/api/content")
                .queryParam("type", TypeContent.ALL.name())
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
        ).andExpect(status().isOk())
        .andExpect(content().json(JSON_CONTENTS));

        verify(contentService, times(1))
                .findContentsByType(eq(TypeContent.ALL));
    }

    @Test
    @WithMockUser
    public void shouldCorrectReturnNewContents() throws Exception {
        given(contentService.findContentsByType(eq(TypeContent.NEW)))
                .willReturn(contents);

        mvc.perform(get("/api/content")
                        .queryParam("type", TypeContent.NEW.name())
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                ).andExpect(status().isOk())
                .andExpect(content().json(JSON_CONTENTS));

        verify(contentService, times(1))
                .findContentsByType(eq(TypeContent.NEW));
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldCorrectReturnPopularContents() throws Exception {
        given(contentService.findContentsByType(eq(TypeContent.POPULAR)))
                .willReturn(contents);

        mvc.perform(get("/api/content")
                        .queryParam("type", TypeContent.POPULAR.name())
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                ).andExpect(status().isOk())
                .andExpect(content().json(JSON_CONTENTS));

        verify(contentService, times(1))
                .findContentsByType(eq(TypeContent.POPULAR));
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldCorrectReturnRecommendedContents() throws Exception {
        given(contentService.findContentsByType(eq(TypeContent.RECOMMENDED)))
                .willReturn(contents);

        mvc.perform(get("/api/content")
                        .queryParam("type", TypeContent.RECOMMENDED.name())
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                ).andExpect(status().isOk())
                .andExpect(content().json(JSON_CONTENTS));

        verify(contentService, times(1))
                .findContentsByType(eq(TypeContent.RECOMMENDED));
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldCorrectReturnContentsByName() throws Exception {
        given(contentService.findContentsByName(eq("unknown")))
                .willReturn(contents);

        mvc.perform(get("/api/content/unknown")
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                ).andExpect(status().isOk())
                .andExpect(content().json(JSON_CONTENTS));

        verify(contentService, times(1))
                .findContentsByName(eq("unknown"));
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldCorrectReturnContentById() throws Exception {
        given(contentService.findContentById(eq(1L)))
                .willReturn(contents.get(0));

        mvc.perform(get("/api/content/id/1")
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
        ).andExpect(status().isOk())
        .andExpect(content().json(JSON_CONTENT));

        verify(contentService, times(1))
                .findContentById(eq(1L));
    }

    @Test
    @WithMockUser(username = "user")
    public void shouldCorrectSaveContentWithContentMediaAndSubtitle() throws Exception {
        given(contentService.save(any(ContentWithContentMediaAndMediaSubtitleDto.class)))
                .willReturn(ContentDto.builder().build());

        mvc.perform(post("/api/content")
                        .with(csrf())
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .content(JSON_CONTENT_WITH_CONTENT_MEDIA_AND_MEDIA_SUBTITLE)
        ).andExpect(status().isCreated())
        .andExpect(content().string("OK"));

        verify(contentService, times(1))
                .save(any(ContentWithContentMediaAndMediaSubtitleDto.class));
    }
}
