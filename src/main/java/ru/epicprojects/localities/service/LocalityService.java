package ru.epicprojects.localities.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.LocalityDTO;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;
import ru.epicprojects.localities.exceptions.InvalidFieldException;
import ru.epicprojects.localities.repositories.AttractionRepository;
import ru.epicprojects.localities.repositories.LocalityRepository;
import ru.epicprojects.localities.utils.LocalityUtil;
import ru.epicprojects.localities.validate.LocalityValidator;

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
            throws EntityIsAlreadyPresentException, InvalidFieldException {
        log.info("Adding locality: {}", localityDTO);
        LocalityValidator.isValid(localityDTO);


        if(localityRepository.findByLocalityAndRegion(
                localityDTO.getLocality(), localityDTO.getRegion()).isPresent()){
            String message = "Can't insert. Locality with name:"+localityDTO.getLocality()+" in region:"
                                +localityDTO.getRegion()+" already existing.";
            log.error(message);
            throw new EntityIsAlreadyPresentException(message);
        }

        LocalityEntity localityEntity = LocalityUtil.toEntity(localityDTO);
        LocalityDTO savedLocalityDTO = LocalityUtil.toDTO(localityRepository.save(localityEntity));

        log.info("Locality added successfully: {}", savedLocalityDTO);
        return savedLocalityDTO;
    }
}
