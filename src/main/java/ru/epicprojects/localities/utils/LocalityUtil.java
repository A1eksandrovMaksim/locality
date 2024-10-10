package ru.epicprojects.localities.utils;

import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.LocalityDTO;

public class LocalityUtil {

    public static LocalityEntity fromDTO(LocalityDTO localityDTO){
        LocalityEntity localityEntity = new LocalityEntity();
        localityEntity.setLocality(localityDTO.getLocality());
        localityEntity.setId(localityDTO.getId());
        localityEntity.setRegion((localityDTO.getRegion()));
        return localityEntity;
    }

    public static LocalityDTO fromEntity(LocalityEntity localityEntity){
        LocalityDTO localityDTO = new LocalityDTO(
                localityEntity.getId(),
                localityEntity.getLocality(),
                localityEntity.getRegion(),

                localityEntity.getAttractions().stream()
                        .map(attractionEntity -> attractionEntity.getId())
                        .toList()
        );
        return localityDTO;
    }
}
