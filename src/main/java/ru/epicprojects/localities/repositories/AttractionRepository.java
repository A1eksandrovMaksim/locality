package ru.epicprojects.localities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.epicprojects.localities.dao.AssistanceEntity;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dto.AttractionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с достопримечательностями {@link AttractionEntity}.
 *
 * Этот интерфейс предоставляет методы для выполнения операций с данными
 * о достопримечательностях, таких как сохранение, обновление, удаление и поиск
 * достопримечательностей в базе данных.
 * Реализует стандартные методы CRUD из JpaRepository.
 *
 * @see AttractionEntity
 */
@Repository
public interface AttractionRepository extends JpaRepository<AttractionEntity, Long> {

    List<AttractionEntity> findByType(AttractionDTO.Type type);
    Optional<AttractionEntity> findByNameAndLocalityID(String name, Long localityID);
    List<AttractionEntity> findAllByLocalityID(Long localityID);

}
