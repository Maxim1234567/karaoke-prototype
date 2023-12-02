package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.otus.domain.Mark;

import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long> {
    @Modifying
    @Query("delete from Mark m where m.id in (:ids)")
    void deleteAllByIds(List<Long> ids);
}
