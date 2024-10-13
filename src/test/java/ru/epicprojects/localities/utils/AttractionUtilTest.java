package ru.epicprojects.localities.utils;

import org.junit.jupiter.api.Test;
import ru.epicprojects.localities.dao.*;
import ru.epicprojects.localities.dto.AssistanceDTO;
import ru.epicprojects.localities.dto.AttractionDTO;

import java.time.LocalDateTime;
import java.util.Arrays;
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
        attractionDTO.setShortDescription("A fun place for family.");
        attractionDTO.setCreatedAt(new Date());
        attractionDTO.setType(AttractionDTO.Type.CASTLE);

        AssistanceDTO assistanceDTO1 = new AssistanceDTO();
        assistanceDTO1.setExecutor("e1");
        assistanceDTO1.setType(AssistanceDTO.Type.ANIMATOR);

        AssistanceDTO assistanceDTO2 = new AssistanceDTO();
        assistanceDTO2.setExecutor("e2");
        assistanceDTO2.setType(AssistanceDTO.Type.AUTO_EXCURSION);

        attractionDTO.setAssistances(Arrays.asList(assistanceDTO1, assistanceDTO2));

        AttractionEntity result = AttractionUtil.toEntity(attractionDTO);

        assertEquals(attractionDTO.getName(), result.getAttractionCompositeAK().getName());
        assertEquals(attractionDTO.getType(), result.getType());
        assertEquals(attractionDTO.getShortDescription(), result.getShortDescription());

        AssistanceEntity assistanceEntity = result.getAssistances().get(0);
        assertEquals(assistanceDTO1.getExecutor(), assistanceEntity.getExecutor());
        assertEquals(assistanceDTO1.getType(), assistanceEntity.getType());
        assertEquals(assistanceDTO1.getShortDescription(), assistanceEntity.getShortDescription());

        assistanceEntity = result.getAssistances().get(1);
        assertEquals(assistanceDTO2.getExecutor(), assistanceEntity.getExecutor());
        assertEquals(assistanceDTO2.getType(), assistanceEntity.getType());
        assertEquals(assistanceDTO2.getShortDescription(), assistanceEntity.getShortDescription());
    }

    @Test
    void toDTO_ShouldConvertAttractionEntityToAttractionDTO() {

        AttractionEntity attractionEntity = new AttractionEntity();
        AttractionCompositeAK ak = new AttractionCompositeAK();
        ak.setName("Amusement Park");
        attractionEntity.setAttractionCompositeAK(ak);
        attractionEntity.setCreatedAt(new Date());
        attractionEntity.setShortDescription("A fun place for family.");
        attractionEntity.setType(AttractionDTO.Type.CASTLE);

        AssistanceEntity assistanceEntity = new AssistanceEntity();
        assistanceEntity.setId(100L);
        assistanceEntity.setType(AssistanceDTO.Type.AUTO_EXCURSION);
        assistanceEntity.setExecutor("exe");
        attractionEntity.setAssistances(Collections.singletonList(assistanceEntity));

        AttractionDTO result = AttractionUtil.toDTO(attractionEntity);

        assertEquals(attractionEntity.getAttractionCompositeAK().getName(), result.getName());
        assertEquals(attractionEntity.getShortDescription(), result.getShortDescription());
        assertEquals(attractionEntity.getType(), result.getType());
        assertEquals(attractionEntity.getCreatedAt(), result.getCreatedAt());
    }
}
