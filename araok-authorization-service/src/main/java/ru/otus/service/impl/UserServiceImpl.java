package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.UserDto;
import ru.otus.exception.AuthException;
import ru.otus.repository.UserRepository;
import ru.otus.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto save(UserDto user) {
        return UserDto.toDto(
                userRepository.save(UserDto.toDomainObject(user))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getByName(String name) {
        return userRepository.findByNameIgnoreCase(name)
                .map(UserDto::toDto)
                .orElseThrow(AuthException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        return userRepository.findById(id)
                .map(UserDto::toDto)
                .orElseThrow(AuthException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getByPhoneAndPassword(String phone, String password) {
        return userRepository.findByPhoneAndPassword(phone, password)
                .map(UserDto::toDto)
                .orElseThrow(() -> new AuthException("Неправильный номер телефона или пароль"));
    }
}
