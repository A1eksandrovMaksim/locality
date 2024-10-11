package ru.epicprojects.localities.utils;

import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dto.AttractionDTO;

import java.util.List;
import java.util.stream.Collectors;

public class AttractionUtil {

    public static AttractionEntity toEntity(AttractionDTO attractionDTO){
        AttractionEntity entity = new AttractionEntity();
        entity.setName(attractionDTO.getName());
        entity.setCreatedAt(attractionDTO.getCreatedAt());
        entity.setShortDescription(attractionDTO.getShortDescription());
        entity.setType(attractionDTO.getType());
        return entity;
    }

    public static AttractionDTO toDTO(AttractionEntity attractionEntity){
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
        return attractionDTO;
    }
}
