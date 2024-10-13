package ru.epicprojects.localities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dao.LocalityEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с местоположением {@link LocalityEntity}.
 *
 * Этот интерфейс предоставляет методы для выполнения операций с данными
 * о местоположениях, таких как сохранение, обновление, удаление и поиск
 * местоположений в базе данных.
 * Реализует стандартные методы CRUD из JpaRepository.
 *
 * @see LocalityEntity
 */
@Repository
public interface LocalityRepository extends JpaRepository<LocalityEntity, Long> {
    Optional<LocalityEntity> findByLocalityAndRegion(String locality, String region);
}
