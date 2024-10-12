package ru.epicprojects.localities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.epicprojects.localities.dao.AssistanceEntity;

/**
 * Репозиторий для работы с сущностями {@link AssistanceEntity}.
 *
 * Этот интерфейс предоставляет методы для выполнения операций с данными
 * об услугах, таких как сохранение, обновление, удаление и поиск
 * услуг в базе данных.
 * Реализует стандартные методы CRUD из JpaRepository.
 *
 * @see AssistanceEntity
 */
public interface AssistanceRepository extends JpaRepository<AssistanceEntity, Long> {
}
