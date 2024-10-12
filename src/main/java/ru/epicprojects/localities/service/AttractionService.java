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
import ru.epicprojects.localities.dto.AttractionDTO;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;
import ru.epicprojects.localities.repositories.AssistanceRepository;
import ru.epicprojects.localities.repositories.AttractionRepository;
import ru.epicprojects.localities.repositories.LocalityRepository;
import ru.epicprojects.localities.utils.AttractionUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Сервис для работы с достопремечательностями
 */
@Service
@AllArgsConstructor
@Slf4j
public class AttractionService {

    private final AttractionRepository attractionRepo;
    private final AssistanceRepository assistanceRepo;
    private final LocalityRepository localityRepo;

    /**
     * Добавляет новую достопремечательность.
     *
     * @param attractionDTO объект DTO, представляющий достопремечательность для добавления
     * @return добавленная достопремечательности в виде объекта DTO
     * @throws EntityIsAlreadyPresentException если достопремечательность с тем же идентификатором уже существует
     * @throws EntityNotFoundException если местоположение или услуги не найдены
     */
    @Transactional
    public AttractionDTO addAttraction(AttractionDTO attractionDTO)
        throws EntityIsAlreadyPresentException{
        log.info("Adding attraction: {}", attractionDTO);

        if(attractionRepo.findById(attractionDTO.getId()).isPresent()){
            String message = "Can't insert. Attraction with ID:" + attractionDTO.getId() + " already existing.";
            log.error(message);
            throw new EntityIsAlreadyPresentException(message);
        }

        AttractionEntity attractionEntity = AttractionUtil.toEntity(attractionDTO);

        LocalityEntity localityEntity = localityRepo.findById(attractionDTO.getLocalityId())
                .orElseThrow(()->{
                    String message = "Locality not found for ID: " + attractionDTO.getLocalityId();
                    log.error(message);
                    return new EntityNotFoundException(message);
                });
        attractionEntity.setLocality(localityEntity);

        List<AssistanceEntity> assistances = new ArrayList<>();
        for (Long assistanceId : attractionDTO.getAssistanceIds()){
            AssistanceEntity assistance = assistanceRepo.findById(assistanceId)
                    .orElseThrow(() -> {
                        String message = "Assistance not found for ID: " + assistanceId;
                        log.error(message);
                        return new EntityNotFoundException(message);
                    });
            assistances.add(assistance);
        }
        attractionEntity.setAssistances(assistances);

        AttractionDTO savedAttractionDTO = AttractionUtil.toDTO(attractionRepo.save(attractionEntity));
        log.info("Attraction added successfully: {}", savedAttractionDTO);

        return savedAttractionDTO;
    }

    /**
     * Показывает все достопремечательности с поддержкой пагинации.
     *
     * @param pageable объект Pageable для определения параметров пагинации
     * @return страница объектов DTO с достопремечательностями
     */
    @Transactional
    public Page<AttractionDTO> showAllAttractions(Pageable pageable) {
        log.info("Fetching all attractions with pagination: {}", pageable);
        Page<AttractionDTO> attractionsPage = attractionRepo.findAll(pageable).map(AttractionUtil::toDTO);
        log.info("Fetched {} attractions", attractionsPage.getTotalElements());
        return attractionsPage;
    }


    /**
     * Показывает все достопремечательности в заданном локалитете.
     *
     * @param localityId идентификатор локалитета
     * @return список объектов DTO с аттракциями, находящимися в указанном локалитете
     * @throws EntityNotFoundException если локалитет с данным идентификатором не найден
     */
    @Transactional
    public List<AttractionDTO> showAllAttractionsInLocality(Long localityId){
        log.info("Fetching attractions for locality ID: {}", localityId);
        List<AttractionDTO> attractions = localityRepo.findById(localityId)
                .orElseThrow(()->{
                    String message = "Locality is not found for ID: " + localityId;
                    log.error(message);
                    return new EntityNotFoundException(message);
                }).getAttractions().stream().map(AttractionUtil::toDTO).toList();
        log.info("Fetched {} attractions for locality ID: {}", attractions.size(), localityId);
        return attractions;
    }

    /**
     * Обновляет существующую достопремечательность.
     *
     * @param attractionDTO объект DTO с обновленными данными достопремечательность
     * @return обновленная достопремечательность в виде объекта DTO
     * @throws EntityNotFoundException если достопремечательность с данным идентификатором не найдена
     */
    @Transactional
    public AttractionDTO updateAttraction(AttractionDTO attractionDTO){
        log.info("Updating attraction with ID: {}", attractionDTO.getId());

        AttractionEntity attractionEntity = attractionRepo.findById(attractionDTO.getId())
                .orElseThrow(()->{
                    String message = "Attraction entity is not found with ID: " + attractionDTO.getId();
                    log.error(message);
                    return new EntityNotFoundException(message);
                });
        attractionEntity.setShortDescription(attractionDTO.getShortDescription());
        AttractionDTO updatedAttractionDTO = AttractionUtil.toDTO(attractionRepo.save(attractionEntity));
        log.info("Attraction updated successfully: {}", updatedAttractionDTO);
        return updatedAttractionDTO;
    }

    /**
     * Удаляет достопремечательность.
     *
     * @param id ID достопремечательности
     */
    @Transactional
    public void deleteAttraction(Long id){
        log.info("Deleting attraction with ID:{}", id);
        attractionRepo.deleteById(id);
        log.info("Attraction with ID:{} deleted successfully", id);
    }
}
