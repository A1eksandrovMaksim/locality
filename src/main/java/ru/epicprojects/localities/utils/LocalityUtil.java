package ru.epicprojects.localities.utils;

import lombok.extern.slf4j.Slf4j;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dao.LocalityCompositeAK;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.AttractionDTO;
import ru.epicprojects.localities.dto.LocalityDTO;

import java.util.Collections;
import java.util.List;

/**
 * Утилитный класс для преобразования между объектами DTO и сущностями
 * локаций.
 *
 * Этот класс предоставляет статические методы для конвертации объектов
 * {@link LocalityDTO} в {@link LocalityEntity} и наоборот.
 */
@Slf4j
public class LocalityUtil {

    /**
     * Преобразует объект DTO {@link LocalityDTO} в сущность {@link LocalityEntity}.
     *
     * @param localityDTO объект DTO, представляющий локацию
     * @return сущность, соответствующая переданному DTO
     */
    public static LocalityEntity toEntity(LocalityDTO localityDTO){
        log.debug("Converting LocalityDTO to LocalityEntity: {}", localityDTO);
        LocalityEntity localityEntity = new LocalityEntity();
        LocalityCompositeAK ak = new LocalityCompositeAK(
                localityDTO.getLocality(),
                localityDTO.getRegion()
        );
        List<AttractionEntity> attractions = Collections.emptyList();
        if(localityDTO.getAttractions() != null)
            attractions = localityDTO.getAttractions().stream().map(AttractionUtil::toEntity).toList();

        localityEntity.setAttractions(attractions);
        localityEntity.setLocalityCompositeAK(ak);
        log.debug("Converted to LocalityEntity: {}", localityEntity);
        return localityEntity;
    }

    /**
     * Преобразует сущность {@link LocalityEntity} в объект DTO {@link LocalityDTO}.
     *
     * @param localityEntity сущность локации для преобразования
     * @return объект DTO, представляющий локации
     */
    public static LocalityDTO toDTO(LocalityEntity localityEntity){
        log.debug("Converting LocalityEntity to LocalityDTO: {}", localityEntity);

        List<AttractionDTO> attractions = Collections.emptyList();
        if(localityEntity.getAttractions() != null)
             attractions = localityEntity.getAttractions().stream().map(AttractionUtil::toDTO).toList();

        LocalityDTO localityDTO = new LocalityDTO(
                localityEntity.getLocalityCompositeAK().getLocality(),
                localityEntity.getLocalityCompositeAK().getRegion(),
                attractions
        );
        log.debug("Converted to LocalityDTO: {}", localityDTO);
        return localityDTO;
    }
}
