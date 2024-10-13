package ru.epicprojects.localities.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epicprojects.localities.dao.AssistanceEntity;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.AssistanceDTO;
import ru.epicprojects.localities.dto.AttractionDTO;
import ru.epicprojects.localities.dto.LocalityDTO;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;
import ru.epicprojects.localities.exceptions.InvalidFieldException;
import ru.epicprojects.localities.repositories.AssistanceRepository;
import ru.epicprojects.localities.repositories.AttractionRepository;
import ru.epicprojects.localities.repositories.LocalityRepository;
import ru.epicprojects.localities.utils.AttractionUtil;
import ru.epicprojects.localities.validate.AttractionValidator;
import ru.epicprojects.localities.validate.LocalityValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Сервис для работы с достопримечательностями
 */
@Service
@AllArgsConstructor
@Slf4j
public class AttractionService {

    private final AttractionRepository attractionRepository;
    private final AssistanceRepository assistanceRepository;
    private final LocalityRepository localityRepository;

    /**
     * Добавляет новую достопримечательность.
     */
    @Transactional
    public AttractionDTO addAttraction(AttractionDTO attractionDTO, LocalityDTO localityDTO)
            throws EntityIsAlreadyPresentException, InvalidFieldException {
        log.info("Adding attraction {} in locality {}",attractionDTO, localityDTO);
        LocalityValidator.isValid(localityDTO);
        AttractionValidator.isValid(attractionDTO);

        String localityName = localityDTO.getLocality();
        String localityRegion = localityDTO.getRegion();

        Optional<LocalityEntity> localityEntity
                = localityRepository.findByLocalityAndRegion(localityName, localityRegion);
        if(localityEntity.isEmpty()){
            String message = "Can't find Location with location:"+localityName+" and region:"+localityRegion;
            log.error(message);
            throw new EntityNotFoundException(message);
        }

        if(attractionRepository.findByNameAndLocalityID(
                attractionDTO.getName(), localityEntity.get().getId()).isPresent()){
            String message = "Can't insert. Attraction with name:" + attractionDTO.getName()
                    + " in a locality:"+localityName+" in a region:"+localityRegion+" already existing.";
            log.error(message);
            throw new EntityIsAlreadyPresentException(message);
        }

        AttractionEntity attractionEntity = AttractionUtil.toEntity(attractionDTO);
        attractionEntity.getAttractionCompositeAK().setLocalityId(localityEntity.get().getId());


        attractionEntity = attractionRepository.save(attractionEntity);

        for(AssistanceEntity assistanceEntity : attractionEntity.getAssistances()){
            if(assistanceRepository.findByExecutorAndType(
                    assistanceEntity.getExecutor(), assistanceEntity.getType()).isEmpty()){
                assistanceEntity.getAttractions().add(attractionEntity);
                assistanceRepository.save(assistanceEntity);
            }
        }
        AttractionDTO savedAttractionDTO = AttractionUtil.toDTO(attractionEntity);
        log.info("Attraction added successfully: {}", savedAttractionDTO);
        return savedAttractionDTO;
    }

    /**
     * Показывает все достопримечательности с поддержкой пагинации.
     *
     * @param pageable объект Pageable для определения параметров пагинации
     * @return страница объектов DTO с достопримечательностями
     */
    @Transactional
    public Page<AttractionDTO> showAllAttractions(Pageable pageable) {
        log.info("Fetching all attractions with pagination: {}", pageable);
        Page<AttractionDTO> attractionsPage = attractionRepository.findAll(pageable).map(AttractionUtil::toDTO);
        log.info("Fetched {} attractions", attractionsPage.getTotalElements());
        return attractionsPage;
    }


    /**
     * Показывает все достопремечательности в заданном локалитете.
     *
     * @return список объектов DTO с аттракциями, находящимися в указанном локалитете
     * @throws EntityNotFoundException если локалитет с данным идентификатором не найден
     */
    @Transactional
    public List<AttractionDTO> showAllAttractionsInLocality(LocalityDTO localityDTO) throws InvalidFieldException {
        LocalityValidator.isValid(localityDTO);
        String localityName = localityDTO.getLocality();
        String localityRegion = localityDTO.getRegion();
        log.info("Fetching attractions for locality:{} in region:{}", localityName, localityRegion);
        Optional<LocalityEntity> locality = localityRepository.findByLocalityAndRegion(localityName, localityRegion);
        if(locality.isEmpty()){
            String message = "Locality is not found for name:" + localityName+" in region:"+localityRegion;
            log.error(message);
            throw new EntityNotFoundException(message);
        }
        List<AttractionEntity> attractionEntities = attractionRepository.findAllByLocalityID(locality.get().getId());
        List<AttractionDTO> attractions = attractionEntities.stream().map(AttractionUtil::toDTO).toList();

        log.info("Fetched {} attractions for locality: {}", attractions.size(), localityName);
        return attractions;
    }

    /**
     * Обновляет существующую достопримечательность.
     *
     * @param attractionDTO объект DTO с обновленными данными достопримечательность
     * @return обновленная достопримечательность в виде объекта DTO
     * @throws EntityNotFoundException если достопримечательность с данным идентификатором не найдена
     */
    @Transactional
    public AttractionDTO updateAttraction(AttractionDTO attractionDTO, LocalityDTO localityDTO) throws InvalidFieldException {
        log.info("Updating attraction {} in locality {}",attractionDTO, localityDTO);
        LocalityValidator.isValid(localityDTO);
        AttractionValidator.isValid(attractionDTO);


        Optional<LocalityEntity> localityEntity
                = localityRepository.findByLocalityAndRegion(localityDTO.getLocality(), localityDTO.getRegion());
        if(localityEntity.isEmpty()){
            String message = "Can't find Location with location:"+localityDTO.getLocality()
                    +" and region:"+localityDTO.getRegion();
            log.error(message);
            throw new EntityNotFoundException(message);
        }

        Optional<AttractionEntity> attractionEntity =
                attractionRepository.findByNameAndLocalityID(attractionDTO.getName(), localityEntity.get().getId());
        if(attractionEntity.isEmpty()){
            String message = "Attraction entity is not found with name:"
                    + attractionDTO.getName()+" in location:"+localityEntity.get().getLocalityCompositeAK().getLocality();
            log.error(message);
            throw new EntityNotFoundException(message);
        }

        attractionEntity.get().setShortDescription(attractionDTO.getShortDescription());
        AttractionDTO updatedAttractionDTO = AttractionUtil.toDTO(attractionRepository.save(attractionEntity.get()));
        log.info("Attraction updated successfully: {}", updatedAttractionDTO);
        return updatedAttractionDTO;
    }

    /**
     * Удаляет достопремечательность.
     */
    @Transactional
    public void deleteAttraction(String attractionName, LocalityDTO localityDTO) throws InvalidFieldException {
        log.info("Deleting attraction {} in locality {}",attractionName, localityDTO);
        LocalityValidator.isValid(localityDTO);

        Optional<LocalityEntity> localityEntity
                = localityRepository.findByLocalityAndRegion(localityDTO.getLocality(), localityDTO.getRegion());
        if(localityEntity.isEmpty()){
            String message = "Can't find Location with location:"+localityDTO.getLocality()
                    +" and region:"+localityDTO.getRegion();
            log.error(message);
            throw new EntityNotFoundException(message);
        }

        AttractionEntity attractionEntity = attractionRepository.findByNameAndLocalityID(attractionName, localityEntity.get().getId())
                .orElseThrow(()->{
                    String message = "Attraction entity is not found with name:"
                            +attractionName+" in location:"+localityEntity.get().getLocalityCompositeAK().getLocality();
                    log.error(message);
                    return new EntityNotFoundException(message);
                });

        attractionRepository.deleteById(attractionEntity.getId());
        log.info("Attraction with name:{} in locality:{} deleted successfully.",attractionName, localityDTO.getLocality());
    }
}
