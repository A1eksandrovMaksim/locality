package ru.epicprojects.localities.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.epicprojects.localities.dao.AssistanceEntity;
import ru.epicprojects.localities.dao.AttractionCompositeAK;
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.AssistanceDTO;
import ru.epicprojects.localities.dto.AttractionDTO;
import ru.epicprojects.localities.dto.LocalityDTO;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;
import ru.epicprojects.localities.exceptions.InvalidFieldException;
import ru.epicprojects.localities.repositories.AssistanceRepository;
import ru.epicprojects.localities.repositories.AttractionRepository;
import ru.epicprojects.localities.repositories.LocalityRepository;
import ru.epicprojects.localities.utils.AttractionUtil;
import ru.epicprojects.localities.utils.LocalityUtil;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AttractionServiceTest {

    @Mock
    private AttractionRepository attractionRepository;

    @Mock
    private AssistanceRepository assistanceRepository;

    @Mock
    private LocalityRepository localityRepository;

    @InjectMocks
    private AttractionService attractionService;

    private AttractionDTO attractionDTO;
    private LocalityDTO localityDTO;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        attractionDTO = new AttractionDTO();
        attractionDTO.setName("casle");
        attractionDTO.setType(AttractionDTO.Type.CASTLE);
        attractionDTO.setCreatedAt(new Date());
        attractionDTO.setShortDescription("This is castle");

        localityDTO = new LocalityDTO("Locality", "Region", Collections.singletonList(attractionDTO));
    }

    @Test
    void addAttraciton_ShouldReturnAttractionDTO_WhenSuccessful() throws EntityIsAlreadyPresentException, InvalidFieldException {
        when(localityRepository.findByLocalityAndRegion(anyString(), anyString())).
                thenReturn(Optional.of(LocalityUtil.toEntity(localityDTO)));
        when(attractionRepository.findByNameAndLocalityID(any(String.class), any(Long.class))).thenReturn(Optional.empty());
        when(assistanceRepository.findById(any())).thenReturn(Optional.empty());
        when(attractionRepository.save(any())).thenAnswer(invoc -> invoc.getArgument(0));
        AttractionDTO result = attractionService.addAttraction(attractionDTO, localityDTO);
        assertNotNull(result);
        verify(attractionRepository, times(1)).save(any());
    }

    @Test
    void showAllAttractions_ShouldReturnPageOfAttractions() {
        LocalityEntity localityEntity = LocalityUtil.toEntity(localityDTO);

        AttractionEntity attraction1 = new AttractionEntity();
        attraction1.setId(1L);
        attraction1.setAttractionCompositeAK(new AttractionCompositeAK("a1", localityEntity.getId()));
        attraction1.setAssistances(Collections.emptyList());

        AttractionEntity attraction2 = new AttractionEntity();
        attraction2.setId(2L);
        attraction2.setAttractionCompositeAK(new AttractionCompositeAK("a2", localityEntity.getId()));
        attraction2.setAssistances(Collections.emptyList());

        List<AttractionEntity> attractions = Arrays.asList(attraction1, attraction2);

        Pageable pageable = PageRequest.of(0, 10); // Запрос на первую страницу с размером 10
        Page<AttractionEntity> page = new PageImpl<>(attractions, pageable, attractions.size());

        when(attractionRepository.findAll(pageable)).thenReturn(page);
        Page<AttractionDTO> result = attractionService.showAllAttractions(pageable);

        assertEquals(2, result.getTotalElements()); // Проверяем общее количество элементов
        assertEquals(2, result.getContent().size()); // Проверяем количество элементов на странице

        assertEquals(attraction1.getAttractionCompositeAK().getName(), result.getContent().get(0).getName());
        assertEquals(attraction2.getAttractionCompositeAK().getName(), result.getContent().get(1).getName());
    }

    @Test
    void showAllAttractionsInLocality_ShouldReturnListOfAttractions_WhenLocalityExists() throws InvalidFieldException {
        LocalityEntity localityEntity = LocalityUtil.toEntity(localityDTO);

        AttractionEntity attraction1 = new AttractionEntity();
        attraction1.setId(1L);
        attraction1.setAttractionCompositeAK(new AttractionCompositeAK("a1", localityEntity.getId()));
        attraction1.setAssistances(Collections.emptyList());

        AttractionEntity attraction2 = new AttractionEntity();
        attraction2.setId(2L);
        attraction2.setAttractionCompositeAK(new AttractionCompositeAK("a2", localityEntity.getId()));
        attraction2.setAssistances(Collections.emptyList());

        when(localityRepository.findByLocalityAndRegion(localityDTO.getLocality(), localityDTO.getRegion())).thenReturn(Optional.of(localityEntity));
        when(attractionRepository.findAllByLocalityID(localityEntity.getId())).thenReturn(Arrays.asList(attraction1, attraction2));
        List<AttractionDTO> result = attractionService.showAllAttractionsInLocality(localityDTO);

        assertEquals(2, result.size());
        assertEquals(attraction1.getAttractionCompositeAK().getName(), result.get(0).getName());
        assertEquals(attraction2.getAttractionCompositeAK().getName(), result.get(1).getName());
    }

    @Test
    void showAllAttractionsInLocality_ShouldThrowException_WhenLocalityDoesNotExist() {
        Long localityId = 1L;

        when(localityRepository.findById(localityId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            attractionService.showAllAttractionsInLocality(localityDTO);
        });

        String expectedMessage = "Locality is not found for name:" + localityDTO.getLocality() + " in region:" + localityDTO.getRegion();
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void updateAttraction_ShouldReturnUpdatedAttraction_WhenAttractionExists() throws InvalidFieldException {

        LocalityEntity localityEntity = LocalityUtil.toEntity(localityDTO);
        AttractionEntity attractionEntity = AttractionUtil.toEntity(attractionDTO);
        when(localityRepository.findByLocalityAndRegion(anyString(), anyString())).thenReturn(Optional.of(localityEntity));
        when(attractionRepository.findByNameAndLocalityID(anyString(), anyLong())).thenReturn(Optional.of(attractionEntity));
        when(attractionRepository.save(attractionEntity)).thenReturn(attractionEntity);

        AttractionDTO result = attractionService.updateAttraction(attractionDTO, localityDTO);

        assertEquals("Updated description", result.getShortDescription());
        assertEquals(attractionDTO.getName(), result.getName());
    }

    @Test
    void deleteAttraction_ShouldCallDeleteById_WhenAttractionExists() throws InvalidFieldException {
        LocalityEntity locality = LocalityUtil.toEntity(localityDTO);
        locality.setId(1L);
        AttractionEntity attractionEntity = AttractionUtil.toEntity(attractionDTO);
        attractionEntity.setId(1L);
        when(localityRepository.findByLocalityAndRegion(anyString(), anyString())).thenReturn(Optional.of(locality));
        when(attractionRepository.findByNameAndLocalityID(attractionDTO.getName(), locality.getId())).thenReturn(Optional.of(attractionEntity));

        attractionService.deleteAttraction(attractionDTO.getName(), localityDTO);

        verify(attractionRepository, times(1)).deleteById(1L);
    }
}
