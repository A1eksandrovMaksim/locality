package ru.epicprojects.localities.utils;

import org.junit.jupiter.api.Test;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.LocalityDTO;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalityUtilTest {

    @Test
    void toEntity_ShouldReturnEntity_WhenSuccessful(){
        LocalityDTO localityDTO = new LocalityDTO(
                1L,
                "locality",
                "region",
                Arrays.asList(1L, 2L)
        );

        LocalityEntity localityEntity = LocalityUtil.toEntity(localityDTO);

        assertEquals(localityDTO.getId(), localityEntity.getId());
        assertEquals(localityDTO.getLocality(), localityEntity.getLocality());
        assertEquals(localityDTO.getRegion(), localityEntity.getRegion());
        assertEquals(null, localityEntity.getAttractions());
    }

    @Test
    void toDTO_ShouldReturnDTO_WhenSuccessful(){
        var attractions = Arrays.asList(new AttractionEntity(), new AttractionEntity());
        attractions.get(0).setId(1L);
        attractions.get(1).setId(2L);

        LocalityEntity localityEntity = new LocalityEntity(
                1L,
                "locality",
                "region",
                attractions
        );

        LocalityDTO localityDTO = LocalityUtil.toDTO(localityEntity);

        assertEquals(localityDTO.getId(), localityEntity.getId());
        assertEquals(localityDTO.getLocality(), localityEntity.getLocality());
        assertEquals(localityDTO.getRegion(), localityEntity.getRegion());
        assertEquals(
                attractions.stream().map(attraction -> attraction.getId()).toList(),
                localityDTO.getAttractionIds()
        );
    }

}
