package ru.otus.service;


import ru.otus.dto.SettingDto;

public interface SettingService {
    SettingDto save(SettingDto setting);

    SettingDto findByContentId(long contentId);
}
