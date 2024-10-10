package ru.epicprojects.localities.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.epicprojects.localities.dao.LocalityEntity;

import java.util.Optional;

@Repository
public interface LocalityRepository extends JpaRepository<LocalityEntity, Long> {


}
