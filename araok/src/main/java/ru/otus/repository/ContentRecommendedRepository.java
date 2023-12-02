package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.domain.ContentRecommended;

public interface ContentRecommendedRepository extends JpaRepository<ContentRecommended, Long> {
}
