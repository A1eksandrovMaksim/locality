package ru.epicprojects.localities.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.LocalityDTO;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;
import ru.epicprojects.localities.repositories.AttractionRepository;
import ru.epicprojects.localities.repositories.LocalityRepository;
import ru.epicprojects.localities.utils.LocalityUtil;

import java.util.List;

/**
 * Сервис для работы с местоположением.
 * Предоставляет операции для добавления и обновления местоположений.
 */
@Service
@AllArgsConstructor
@Slf4j
public class LocalityService {

    private final LocalityRepository localityRepository;
    private final AttractionRepository attractionRepository;

    /**
     * Добавляет новое местоположение.
     *
     * @param localityDTO объект DTO, представляющий местоположение для добавления
     * @return добавленный местоположение в виде объекта DTO
     * @throws EntityIsAlreadyPresentException если местоположение с тем же идентификатором уже существует
     */
    @Transactional
    public LocalityDTO addLocality (LocalityDTO localityDTO)
        throws EntityIsAlreadyPresentException{
        log.info("Adding locality: {}", localityDTO);

        if(localityRepository.findById(localityDTO.getId()).isPresent()){
            String message = "Can't insert. Locality with ID:"+localityDTO.getId()+" already existing.";
            log.error(message);
            throw new EntityIsAlreadyPresentException(
                    "Can't insert. Locality with ID:"+localityDTO.getId()+" already existing."
            );
        }

        LocalityEntity localityEntity = LocalityUtil.toEntity(localityDTO);
        List<AttractionEntity> attractions =
                attractionRepository.findAllById(localityDTO.getAttractionIds());
        localityEntity.setAttractions(attractions);

        LocalityDTO savedLocalityDTO = LocalityUtil.toDTO(localityRepository.save(localityEntity));
        log.info("Locality added successfully: {}", savedLocalityDTO);
        return savedLocalityDTO;
    }

    /**
     * Обновляет существующий местоположение.
     *
     * @param localityDTO объект DTO, представляющий местоположение для обновления
     * @return обновленный местоположение в виде объекта DTO
     * @throws EntityNotFoundException если местоположение с указанным идентификатором не найден
     */
    @Transactional
    public LocalityDTO updateLocality (LocalityDTO localityDTO){
        log.info("Updating locality: {}", localityDTO);

        LocalityEntity localityEntity = localityRepository.findById(localityDTO.getId())
                .orElseThrow(()->{
                    String message = "Entity is not found by ID: " + localityDTO.getId();
                    log.error(message);
                    return new EntityNotFoundException(message);
                });
        List<AttractionEntity> attractions =
                attractionRepository.findAllById(localityDTO.getAttractionIds());
        localityEntity.setAttractions(attractions);

        LocalityDTO updatedLocalityDTO = LocalityUtil.toDTO(localityRepository.save(localityEntity));
        log.info("Locality updated successfully: {}", updatedLocalityDTO);
        return updatedLocalityDTO;
    }
}
