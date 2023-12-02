package ru.otus.service;


import ru.otus.dto.LanguageDto;
import ru.otus.dto.MediaSubtitleDto;

import java.util.List;

public interface MediaSubtitleService {

    MediaSubtitleDto save(MediaSubtitleDto mediaSubtitleD);

    MediaSubtitleDto findMediaSubtitleByContentIdAndLanguageId(long contentId, long languageId);

    List<LanguageDto> findAllLanguageSubtitleByContentId(long contentId);
}
