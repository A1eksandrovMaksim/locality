package ru.epicprojects.localities.utils;

import org.junit.jupiter.api.Test;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dao.LocalityCompositeAK;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.LocalityDTO;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocalityUtilTest {

    @Test
    void toEntity_ShouldReturnEntity_WhenSuccessful(){
        LocalityDTO localityDTO = new LocalityDTO();
        localityDTO.setLocality("locality");
        localityDTO.setRegion("region");
        localityDTO.setAttractions(Collections.emptyList());

        LocalityEntity localityEntity = LocalityUtil.toEntity(localityDTO);

        assertEquals(localityDTO.getLocality(), localityEntity.getLocalityCompositeAK().getLocality());
        assertEquals(localityDTO.getRegion(), localityEntity.getLocalityCompositeAK().getRegion());
    }

    @Test
    void toDTO_ShouldReturnDTO_WhenSuccessful(){
        var attractions = Arrays.asList(new AttractionEntity(), new AttractionEntity());
        attractions.get(0).setId(1L);
        attractions.get(1).setId(2L);

        LocalityEntity localityEntity = new LocalityEntity();
        localityEntity.setId(1L);
        localityEntity.setLocalityCompositeAK(new LocalityCompositeAK("locality", "region"));

        LocalityDTO localityDTO = LocalityUtil.toDTO(localityEntity);

        assertEquals(localityDTO.getLocality(), localityEntity.getLocalityCompositeAK().getLocality());
        assertEquals(localityDTO.getRegion(), localityEntity.getLocalityCompositeAK().getRegion());
    }

}
