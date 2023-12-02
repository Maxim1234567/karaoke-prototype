package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.domain.Content;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long> {
    @Query("select c from Content c where (c.createDate + 7 DAY) >= CURRENT_DATE")
    List<Content> findByCreateDateLessThanNow();

    List<Content> findByNameContainingIgnoreCase(String name);

    Optional<Content> findById(Long id);
}
