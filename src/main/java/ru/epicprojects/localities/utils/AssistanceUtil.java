package ru.epicprojects.localities.utils;

import lombok.extern.slf4j.Slf4j;
import ru.epicprojects.localities.dao.AssistanceEntity;
import ru.epicprojects.localities.dto.AssistanceDTO;

/**
 * Служебный класс для конвертирования классов услуг,
 * представленных DTO и сущностями
 */
@Slf4j
public class AssistanceUtil {

    public static AssistanceDTO toDTO(AssistanceEntity assistanceEntity){
        log.debug("Converting AssistanceEntity to AssistanceDTO: {}", assistanceEntity);
        AssistanceDTO assistanceDTO = new AssistanceDTO(
                assistanceEntity.getType(),
                assistanceEntity.getShortDescription(),
                assistanceEntity.getExecutor()
        );
        log.debug("Converted to AssistanceDTO: {}", assistanceDTO);
        return assistanceDTO;
    }

    public static AssistanceEntity toEntity(AssistanceDTO assistanceDTO){
        log.debug("Conversting AssistanceDTO to AssistanceEntity: {}", assistanceDTO);
        AssistanceEntity assistanceEntity = new AssistanceEntity();
        assistanceEntity.setType(assistanceDTO.getType());
        assistanceEntity.setShortDescription(assistanceDTO.getShortDescription());
        assistanceEntity.setExecutor(assistanceDTO.getExecutor());
        log.debug("Converted AssistanceEntity to AssistanceDTO: {}", assistanceEntity);
        return assistanceEntity;
    }
}
