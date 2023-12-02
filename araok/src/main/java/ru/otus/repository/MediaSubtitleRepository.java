package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.domain.Language;
import ru.otus.domain.MediaSubtitle;

import java.util.List;
import java.util.Optional;

public interface MediaSubtitleRepository extends JpaRepository<MediaSubtitle, Long> {
    @Query("select ms from MediaSubtitle ms where ms.content.id = :contentId")
    List<MediaSubtitle> findByContentId(@Param("contentId") long contentId);

    @Query("select ms from MediaSubtitle ms where ms.language.id = :languageId")
    List<MediaSubtitle> findByLanguageId(@Param("languageId") long languageId);

    @Query("select ms from MediaSubtitle ms where ms.content.id = :contentId and ms.language.id = :languageId")
    Optional<MediaSubtitle> findByContentIdAndLanguageId(
            @Param("contentId") long contentId,
            @Param("languageId") long languageId
    );

    @Query("select ms.language from MediaSubtitle ms where ms.content.id = :contentId")
    List<Language> findAllLanguageSubtitleByContentId(@Param("contentId") long contentId);
}
