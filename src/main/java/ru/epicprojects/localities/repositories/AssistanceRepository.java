package ru.epicprojects.localities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epicprojects.localities.dao.AssistanceEntity;
import ru.epicprojects.localities.dto.AssistanceDTO;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностями {@link AssistanceEntity}.
 */
public interface AssistanceRepository extends JpaRepository<AssistanceEntity, Long> {
    List<AssistanceEntity> saveAll(AssistanceEntity ... entities);
    Optional<AssistanceEntity>  findByExecutorAndType(String executor, AssistanceDTO.Type type);
}
