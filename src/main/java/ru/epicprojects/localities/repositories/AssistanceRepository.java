package ru.epicprojects.localities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epicprojects.localities.dao.AssistanceEntity;

public interface AssistanceRepository extends JpaRepository<AssistanceEntity, Long> {
}
