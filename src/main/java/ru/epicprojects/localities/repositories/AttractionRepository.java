package ru.epicprojects.localities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dto.AttractionDTO;

import java.util.List;

@Repository
public interface AttractionRepository extends JpaRepository<AttractionEntity, Long> {

    List<AttractionEntity> findByType(AttractionDTO.Type type);
    List<AttractionEntity> findByName(String name);
}
