package ru.epicprojects.localities.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.epicprojects.localities.dao.AssistanceEntity;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.AttractionDTO;
import ru.epicprojects.localities.repositories.AssistanceRepository;
import ru.epicprojects.localities.repositories.AttractionRepository;
import ru.epicprojects.localities.repositories.LocalityRepository;
import ru.epicprojects.localities.utils.AttractionUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AttractionService {

    private final AttractionRepository attractionRepo;
    private final AssistanceRepository assistanceRepo;
    private final LocalityRepository localityRepo;

    @Transactional
    public AttractionDTO addAttraction(AttractionDTO attractionDTO){
        AttractionEntity attractionEntity = AttractionUtil.fromDTO(attractionDTO);

        LocalityEntity locality = localityRepo.findById(attractionDTO.getLocalityId())
                .orElseThrow(()->new EntityNotFoundException(
                        "Locality not found for ID: "+attractionDTO.getLocalityId()
                ));
        List<AssistanceEntity> assistances = new ArrayList<>();
        for (Long assistanceId : attractionDTO.getAssistanceIds()){
            AssistanceEntity assistance = assistanceRepo.findById(assistanceId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Assistance not found for ID: " + assistanceId
                    ));
            assistances.add(assistance);
        }
        attractionEntity.setAssistances(assistances);

        return AttractionUtil.fromEntity(attractionRepo.save(attractionEntity));
    }

    @Transactional
    public Page<AttractionDTO> showAllAttractions(Pageable pageable) {
        return attractionRepo.findAll(pageable).map(AttractionUtil::fromEntity);
    }

    @Transactional
    public List<AttractionDTO> showAllAttractionsInLocality(Long localityId){
        return localityRepo.findById(localityId)
                .orElseThrow(()->new EntityNotFoundException(
                    "Locality not found for ID: " + localityId
                )).getAttractions().stream().map(AttractionUtil::fromEntity).toList();
    }

    @Transactional
    public void deleteAttraction(AttractionDTO attractionDTO){
        attractionRepo.delete(AttractionUtil.fromDTO(attractionDTO));
    }
}
