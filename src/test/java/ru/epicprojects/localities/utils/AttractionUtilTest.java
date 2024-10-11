package ru.epicprojects.localities.utils;

import org.junit.jupiter.api.Test;
import ru.epicprojects.localities.dao.AssistanceEntity;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.AttractionDTO;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttractionUtilTest {
    @Test
    void toEntity_ShouldConvertAttractionDTOToAttractionEntity() {
        // Подготовка данных для теста
        AttractionDTO attractionDTO = new AttractionDTO();
        attractionDTO.setName("Amusement Park");
        attractionDTO.setCreatedAt(new Date());
        attractionDTO.setShortDescription("A fun place for family.");
        attractionDTO.setType(AttractionDTO.Type.CASTLE);

        AttractionEntity result = AttractionUtil.toEntity(attractionDTO);

        assertEquals(attractionDTO.getName(), result.getName());
        assertEquals(attractionDTO.getCreatedAt(), result.getCreatedAt());
        assertEquals(attractionDTO.getShortDescription(), result.getShortDescription());
        assertEquals(attractionDTO.getType(), result.getType());
    }

    @Test
    void toDTO_ShouldConvertAttractionEntityToAttractionDTO() {
        Long attractionId = 1L;
        LocalDateTime createdAt = LocalDateTime.now();

        LocalityEntity localityEntity = new LocalityEntity();
        localityEntity.setId(10L);

        AttractionEntity attractionEntity = new AttractionEntity();
        attractionEntity.setId(attractionId);
        attractionEntity.setName("Amusement Park");
        attractionEntity.setCreatedAt(new Date());
        attractionEntity.setShortDescription("A fun place for family.");
        attractionEntity.setType(AttractionDTO.Type.CASTLE);
        attractionEntity.setLocality(localityEntity);

        AssistanceEntity assistanceEntity = new AssistanceEntity();
        assistanceEntity.setId(100L);
        attractionEntity.setAssistances(Collections.singletonList(assistanceEntity));

        AttractionDTO result = AttractionUtil.toDTO(attractionEntity);

        assertEquals(attractionEntity.getId(), result.getId());
        assertEquals(attractionEntity.getName(), result.getName());
        assertEquals(attractionEntity.getShortDescription(), result.getShortDescription());
        assertEquals(attractionEntity.getType(), result.getType());
        assertEquals(attractionEntity.getCreatedAt(), result.getCreatedAt());
        assertEquals(attractionEntity.getLocality().getId(), result.getLocalityId());

        List<Long> expectedAssistanceIds = Collections.singletonList(100L);
        assertEquals(expectedAssistanceIds, result.getAssistanceIds()); // Проверяем список Assitance IDs
    }
}
