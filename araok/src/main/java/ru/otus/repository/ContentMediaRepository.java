package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.domain.ContentMedia;
import ru.otus.domain.ContentMediaId;

import java.util.List;
import java.util.Optional;

public interface ContentMediaRepository extends JpaRepository<ContentMedia, ContentMedia> {
    Optional<ContentMedia> findByContentMediaId(ContentMediaId contentMediaId);

    @Query("select cm from ContentMedia cm where cm.contentMediaId.mediaType.id = :typeId")
    List<ContentMedia> findByTypeId(@Param("typeId") Long typeId);

    @Query("select cm.media from ContentMedia cm where cm.contentMediaId = :contentMediaId")
    byte[] findMediaByContentMediaId(ContentMediaId contentMediaId);
}
