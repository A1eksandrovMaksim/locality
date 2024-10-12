package ru.epicprojects.localities.utils;

import lombok.extern.slf4j.Slf4j;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.LocalityDTO;

/**
 * Утилитный класс для преобразования между объектами DTO и сущностями
 * локалитетов.
 *
 * Этот класс предоставляет статические методы для конвертации объектов
 * {@link LocalityDTO} в {@link LocalityEntity} и наоборот.
 */
@Slf4j
public class LocalityUtil {

    /**
     * Преобразует объект DTO {@link LocalityDTO} в сущность {@link LocalityEntity}.
     *
     * @param localityDTO объект DTO, представляющий локалитет
     * @return сущность локалитета, соответствующая переданному DTO
     */
    public static LocalityEntity toEntity(LocalityDTO localityDTO){
        log.debug("Converting LocalityDTO to LocalityEntity: {}", localityDTO);
        LocalityEntity localityEntity = new LocalityEntity();
        localityEntity.setLocality(localityDTO.getLocality());
        localityEntity.setId(localityDTO.getId());
        localityEntity.setRegion((localityDTO.getRegion()));
        log.debug("Converted to LocalityEntity: {}", localityEntity);
        return localityEntity;
    }

    /**
     * Преобразует сущность {@link LocalityEntity} в объект DTO {@link LocalityDTO}.
     *
     * @param localityEntity сущность локалитета для преобразования
     * @return объект DTO, представляющий локалитет
     */
    public static LocalityDTO toDTO(LocalityEntity localityEntity){
        log.debug("Converting LocalityEntity to LocalityDTO: {}", localityEntity);
        LocalityDTO localityDTO = new LocalityDTO(
                localityEntity.getId(),
                localityEntity.getLocality(),
                localityEntity.getRegion(),

                localityEntity.getAttractions().stream()
                        .map(attractionEntity -> attractionEntity.getId())
                        .toList()
        );
        log.debug("Converted to LocalityDTO: {}", localityDTO);
        return localityDTO;
    }
}
