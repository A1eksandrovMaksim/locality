package ru.epicprojects.localities.utils;

import lombok.extern.slf4j.Slf4j;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dto.AttractionDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Утилитный класс для преобразования между объектами DTO и сущностями
 * аттракций.
 *
 * Этот класс предоставляет статические методы для конвертации объектов
 * {@link AttractionDTO} в {@link AttractionEntity} и наоборот.
 */
@Slf4j
public class AttractionUtil {

    /**
     * Преобразует объект DTO {@link AttractionDTO} в сущность {@link AttractionEntity}.
     *
     * @param attractionDTO объект DTO, представляющий аттракцию
     * @return сущность аттракции, соответствующая переданному DTO
     */
    public static AttractionEntity toEntity(AttractionDTO attractionDTO){
        log.debug("Converting AttractionDTO to AttractionEntity: {}", attractionDTO);
        AttractionEntity entity = new AttractionEntity();
        entity.setName(attractionDTO.getName());
        entity.setCreatedAt(attractionDTO.getCreatedAt());
        entity.setShortDescription(attractionDTO.getShortDescription());
        entity.setType(attractionDTO.getType());
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
        attractionDTO.setName(attractionEntity.getName());
        attractionDTO.setType(attractionEntity.getType());
        attractionDTO.setId(attractionEntity.getId());
        attractionDTO.setCreatedAt(attractionEntity.getCreatedAt());
        attractionDTO.setLocalityId(attractionEntity.getLocality().getId());
        attractionDTO.setShortDescription(attractionEntity.getShortDescription());

        List<Long> assistanceIds = attractionEntity.getAssistances().stream()
                .map((assistanceEntity) -> assistanceEntity.getId())
                .collect(Collectors.toList());
        attractionDTO.setAssistanceIds(assistanceIds);
        log.debug("Converted to AttractionDTO: {}", attractionDTO);
        return attractionDTO;
    }
}
