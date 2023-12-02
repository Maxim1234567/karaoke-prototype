package ru.otus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.domain.User;
import ru.otus.dto.UserDto;
import ru.otus.enums.RoleEnum;
import ru.otus.exception.AuthException;
import ru.otus.repository.UserRepository;
import ru.otus.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .name("Maxim")
                .phone("89999999999")
                .password("12345")
                .birthDate(LocalDate.now())
                .role(RoleEnum.USER)
                .build();

        userService = new UserServiceImpl(
                userRepository
        );
    }

    @Test
    public void shouldCorrectSaveUser() {
        given(userRepository.save(any(User.class)))
                .willReturn(user);

        UserDto result = userService.save(UserDto.toDto(user));

        verify(userRepository, times(1)).save(any(User.class));

        assertThatUser(user, result);
    }

    @Test
    public void shouldCorrectReturnUserByName() {
        given(userRepository.findByNameIgnoreCase(eq("maxim")))
                .willReturn(Optional.of(user));

        UserDto result = userService.getByName("maxim");

        verify(userRepository, times(1)).findByNameIgnoreCase(eq("maxim"));

        assertThatUser(user, result);
    }

    @Test
    public void shouldThrowAuthExceptionByNameIfEmpty() {
        given(userRepository.findByNameIgnoreCase(eq("maxim")))
                .willReturn(Optional.empty());

        assertThrows(AuthException.class, () -> userService.getByName("maxim"));

        verify(userRepository, times(1)).findByNameIgnoreCase(eq("maxim"));
    }

    @Test
    public void shouldCorrectReturnUserById() {
        given(userRepository.findById(eq(1L)))
                .willReturn(Optional.of(user));

        UserDto result = userService.getById(1L);

        verify(userRepository, times(1)).findById(eq(1L));

        assertThatUser(user, result);
    }

    @Test
    public void shouldThrowAuthExceptionByIdIfEmpty() {
        given(userRepository.findById(eq(1L)))
                .willReturn(Optional.empty());

        assertThrows(AuthException.class, () -> userService.getById(1L));

        verify(userRepository, times(1)).findById(eq(1L));
    }

    @Test
    public void shouldCorrectReturnUserByNameAndPassword() {
        given(userRepository.findByPhoneAndPassword(eq("89999999999"), eq("12345")))
                .willReturn(Optional.of(user));

        UserDto result = userService.getByPhoneAndPassword("89999999999", "12345");

        verify(userRepository, times(1))
                .findByPhoneAndPassword(eq("89999999999"), eq("12345"));

        assertThatUser(user, result);
    }

    @Test
    public void shouldThrowAuthExceptionByNameAndPassword() {
        given(userRepository.findByPhoneAndPassword(eq("89999999999"), eq("12345")))
                .willReturn(Optional.empty());

        assertThrows(AuthException.class, () -> userService.getByPhoneAndPassword("89999999999", "12345"));

        verify(userRepository, times(1)).findByPhoneAndPassword(eq("89999999999"), eq("12345"));
    }

    private void assertThatUser(User excepted, UserDto result) {
        assertThat(result).isNotNull()
                .matches(u -> u.getId().equals(excepted.getId()))
                .matches(u -> u.getName().equals(excepted.getName()))
                .matches(u -> u.getPhone().equals(excepted.getPhone()))
                .matches(u -> u.getBirthDate().equals(excepted.getBirthDate()))
                .matches(u -> u.getRole().equals(excepted.getRole()));
    }
}
