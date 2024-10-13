package ru.epicprojects.localities.utils;

import lombok.extern.slf4j.Slf4j;
import ru.epicprojects.localities.dao.AssistanceEntity;
import ru.epicprojects.localities.dao.AttractionCompositeAK;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dto.AssistanceDTO;
import ru.epicprojects.localities.dto.AttractionDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Вспомогательный класс для преобразования DTO и сущностями
 * достопримечательностей.
 */
@Slf4j
public class AttractionUtil {

    /**
     * Преобразует объект DTO {@link AttractionDTO} в сущность {@link AttractionEntity}.
     *
     * @param attractionDTO объект DTO, представляющий достопримечательность
     * @return сущность достопримечательности, соответствующая переданному DTO
     */
    public static AttractionEntity toEntity(AttractionDTO attractionDTO){
        log.debug("Converting AttractionDTO to AttractionEntity: {}", attractionDTO);
        AttractionEntity entity = new AttractionEntity();
        AttractionCompositeAK ak = new AttractionCompositeAK();
        ak.setName(attractionDTO.getName());
        entity.setAttractionCompositeAK(ak);
        entity.setShortDescription(attractionDTO.getShortDescription());
        entity.setType(attractionDTO.getType());

        List<AssistanceEntity> assistances = Collections.emptyList();
        if(attractionDTO.getAssistances() != null)
                assistances = attractionDTO.getAssistances().stream().map(AssistanceUtil::toEntity).toList();
        entity.setAssistances(assistances);
        log.debug("Converted to AttractionEntity: {}", entity);
        return entity;
    }

    /**
     * Преобразует сущность {@link AttractionEntity} в объект DTO {@link AttractionDTO}.
     *
     * @param attractionEntity сущность аттракции для преобразования
     * @return объект DTO, представляющий аттракцию
     */
    public static AttractionDTO toDTO(AttractionEntity attractionEntity){
        log.debug("Converting AttractionEntity to AttractionDTO: {}", attractionEntity);
        AttractionDTO attractionDTO = new AttractionDTO();
        attractionDTO.setName(attractionEntity.getAttractionCompositeAK().getName());
        attractionDTO.setType(attractionEntity.getType());
        attractionDTO.setCreatedAt(attractionEntity.getCreatedAt());
        attractionDTO.setShortDescription(attractionEntity.getShortDescription());
        List<AssistanceDTO> assistances = Collections.emptyList();
        if(attractionEntity.getAssistances() != null)
            assistances = attractionEntity.getAssistances().stream().map(AssistanceUtil::toDTO).toList();
        attractionDTO.setAssistances(assistances);
        log.debug("Converted to AttractionDTO: {}", attractionDTO);
        return attractionDTO;
    }
}
