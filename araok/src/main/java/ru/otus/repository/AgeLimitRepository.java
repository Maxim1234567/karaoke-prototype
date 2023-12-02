package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.domain.AgeLimit;

public interface AgeLimitRepository extends JpaRepository<AgeLimit, Long> {

}
