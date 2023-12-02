package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.domain.Setting;

import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    @Query("select s from Setting s where s.content.id = :contentId")
    Optional<Setting> findByContentId(@Param("contentId") long contentId);

    @Modifying
    @Query("delete from Setting s where s.content.id = :contentId")
    void deleteByContentId(@Param("contentId") long contentId);
}
