package ru.otus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.dto.UserDto;
import ru.otus.enums.RoleEnum;
import ru.otus.filter.JwtFilter;
import ru.otus.service.impl.UserAuthentication;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    private static String JSON_USER =
            """
                {
                    "id": 1,
                    "name": "Test",
                    "phone": "89999999999",
                    "password": "12345",
                    "birthDate": "1994-08-05",
                    "role": "USER"
                }
            """;

    @MockBean
    private UserAuthentication userAuthentication;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    private UserDto user;

    @BeforeEach
    public void setUp() {
        user = UserDto.builder()
                .id(1L)
                .name("Test")
                .phone("89999999999")
                .password("12345")
                .birthDate(LocalDate.of(1994, 8, 5))
                .role(RoleEnum.USER)
                .build();

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldCorrectReturnUserById() throws Exception {
        given(userAuthentication.getId())
                .willReturn(user.getId());
        given(userAuthentication.getName())
                .willReturn(user.getName());
        given(userAuthentication.getPassword())
                .willReturn(user.getPassword());
        given(userAuthentication.getPhone())
                .willReturn(user.getPhone());
        given(userAuthentication.getBirthDate())
                .willReturn(user.getBirthDate());
        given(userAuthentication.getRole())
                .willReturn(user.getRole());

        mvc.perform(get("/auth/user")
                .with(csrf())
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
        ).andExpect(status().isOk())
        .andExpect(content().json(JSON_USER));

        verify(userAuthentication, times(1)).getId();
        verify(userAuthentication, times(1)).getName();
        verify(userAuthentication, times(1)).getPassword();
        verify(userAuthentication, times(1)).getPhone();
        verify(userAuthentication, times(1)).getBirthDate();
        verify(userAuthentication, times(1)).getRole();
    }
}