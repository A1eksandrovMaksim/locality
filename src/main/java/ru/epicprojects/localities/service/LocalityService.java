package ru.epicprojects.localities.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.LocalityDTO;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;
import ru.epicprojects.localities.repositories.AttractionRepository;
import ru.epicprojects.localities.repositories.LocalityRepository;
import ru.epicprojects.localities.utils.LocalityUtil;

import java.util.List;

@Service
@AllArgsConstructor
public class LocalityService {

    private final LocalityRepository localityRepository;
    private final AttractionRepository attractionRepository;

    @Transactional
    public LocalityDTO addLocality (LocalityDTO localityDTO)
        throws EntityIsAlreadyPresentException{

        if(localityRepository.findById(localityDTO.getId()).isPresent())
            throw new EntityIsAlreadyPresentException(
                    "Can't instert already existing Locality :" + localityDTO.getId()
            );

        LocalityEntity localityEntity = LocalityUtil.fromDTO(localityDTO);
        List<AttractionEntity> attractions =
                attractionRepository.findAllById(localityDTO.getAttractionIds());
        localityEntity.setAttractions(attractions);
        return LocalityUtil.fromEntity(localityRepository.save(localityEntity));
    }

    @Transactional
    public LocalityDTO updateLocality (LocalityDTO localityDTO){
        LocalityEntity localityEntity = localityRepository.findById(localityDTO.getId())
                .orElseThrow(()->new EntityNotFoundException(
                        "Entity is not found by ID: "+localityDTO.getId()
                ));
        List<AttractionEntity> attractions =
                attractionRepository.findAllById(localityDTO.getAttractionIds());
        localityEntity.setAttractions(attractions);
        return LocalityUtil.fromEntity(localityRepository.save(localityEntity));
    }
}
