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
import ru.epicprojects.localities.dao.AttractionEntity;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.AssistanceDTO;
import ru.epicprojects.localities.dto.AttractionDTO;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;
import ru.epicprojects.localities.repositories.AssistanceRepository;
import ru.epicprojects.localities.repositories.AttractionRepository;
import ru.epicprojects.localities.repositories.LocalityRepository;

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

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        attractionDTO = new AttractionDTO();
        attractionDTO.setId(1L);
        attractionDTO.setName("casle");
        attractionDTO.setType(AttractionDTO.Type.CASTLE);
        attractionDTO.setCreatedAt(new Date());
        List<Long> assistanceIds = Arrays.asList(1L, 2L);
        attractionDTO.setAssistanceIds(assistanceIds);
        attractionDTO.setLocalityId(1L);
        attractionDTO.setShortDescription("This is castle");
    }

    @Test
    void addAttraction_ShouldThrowException_WhenAttractionExists(){
        when(attractionRepository.findById(attractionDTO.getId())).thenReturn(Optional.of(new AttractionEntity()));
        EntityIsAlreadyPresentException exception = assertThrows(EntityIsAlreadyPresentException.class, ()->attractionService.addAttraction(attractionDTO));
        assertEquals("Can't insert. Attraction with ID:1 already existing.", exception.getMessage());
    }

    @Test
    void addAttractin_ShouldThrowException_WhenLocalityIsNotExists(){
        when(attractionRepository.findById(attractionDTO.getId())).thenReturn(Optional.empty());
        when(localityRepository.findById(attractionDTO.getLocalityId())).thenReturn(Optional.empty());
        EntityNotFoundException notFoundException = assertThrows(EntityNotFoundException.class, ()->attractionService.addAttraction(attractionDTO));
        assertEquals("Locality not found for ID: 1", notFoundException.getMessage());
    }

    @Test
    void addAttractin_ShouldThrowException_OneOrMoreAssistancesIsNotExists(){
        when(attractionRepository.findById(attractionDTO.getId())).thenReturn(Optional.empty());
        when(localityRepository.findById(attractionDTO.getLocalityId())).thenReturn(Optional.of(new LocalityEntity()));
        when(assistanceRepository.findById(any())).thenReturn(Optional.of(new AssistanceEntity())).thenReturn(Optional.empty());
        EntityNotFoundException notFoundException = assertThrows(EntityNotFoundException.class, ()->attractionService.addAttraction(attractionDTO));
        assertEquals("Assistance not found for ID: 2", notFoundException.getMessage());
        verify(assistanceRepository, times(2)).findById(any());
    }

    @Test
    void addAttraciton_ShouldReturnAttractionDTO_WhenSuccessful() throws EntityIsAlreadyPresentException {
        when(attractionRepository.findById(attractionDTO.getId())).thenReturn(Optional.empty());
        when(localityRepository.findById(attractionDTO.getLocalityId())).
                thenAnswer(i -> {
                    var le = new LocalityEntity();
                    le.setId(0L);
                    return Optional.of(le);
                });
        when(assistanceRepository.findById(any())).thenReturn(Optional.of(new AssistanceEntity()));
        when(attractionRepository.save(any())).thenAnswer(invoc -> invoc.getArgument(0));
        AttractionDTO result = attractionService.addAttraction(attractionDTO);
        assertNotNull(result);
        verify(attractionRepository, times(1)).save(any());
    }

    @Test
    void showAllAttractions_ShouldReturnPageOfAttractions() {
        AttractionEntity attraction1 = new AttractionEntity();
        attraction1.setId(1L);
        var le1 = new LocalityEntity();
        le1.setId(0L);
        attraction1.setLocality(le1);
        attraction1.setAssistances(Collections.emptyList());

        AttractionEntity attraction2 = new AttractionEntity();
        attraction2.setId(2L);
        var le2 = new LocalityEntity();
        le2.setId(0L);
        attraction2.setLocality(le2);
        attraction2.setAssistances(Collections.emptyList());

        List<AttractionEntity> attractions = Arrays.asList(attraction1, attraction2);

        Pageable pageable = PageRequest.of(0, 10); // Запрос на первую страницу с размером 10
        Page<AttractionEntity> page = new PageImpl<>(attractions, pageable, attractions.size());

        when(attractionRepository.findAll(pageable)).thenReturn(page);
        Page<AttractionDTO> result = attractionService.showAllAttractions(pageable);

        assertEquals(2, result.getTotalElements()); // Проверяем общее количество элементов
        assertEquals(2, result.getContent().size()); // Проверяем количество элементов на странице

        assertEquals(attraction1.getId(), result.getContent().get(0).getId());
        assertEquals(attraction2.getId(), result.getContent().get(1).getId());
    }

    @Test
    void showAllAttractionsInLocality_ShouldReturnListOfAttractions_WhenLocalityExists() {
        LocalityEntity locality = new LocalityEntity();
        locality.setId(1L);

        AttractionEntity attraction1 = new AttractionEntity();
        attraction1.setId(1L);
        var le1 = new LocalityEntity();
        le1.setId(0L);
        attraction1.setLocality(le1);
        attraction1.setAssistances(Collections.emptyList());

        AttractionEntity attraction2 = new AttractionEntity();
        attraction2.setId(2L);
        var le2 = new LocalityEntity();
        le2.setId(0L);
        attraction2.setLocality(le2);
        attraction2.setAssistances(Collections.emptyList());

        locality.setAttractions(List.of(attraction1, attraction2));

        when(localityRepository.findById(locality.getId())).thenReturn(Optional.of(locality));
        List<AttractionDTO> result = attractionService.showAllAttractionsInLocality(locality.getId());

        assertEquals(2, result.size());
        assertEquals(attraction1.getId(), result.get(0).getId());
        assertEquals(attraction2.getId(), result.get(1).getId());
    }

    @Test
    void showAllAttractionsInLocality_ShouldThrowException_WhenLocalityDoesNotExist() {
        Long localityId = 1L;

        when(localityRepository.findById(localityId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            attractionService.showAllAttractionsInLocality(localityId);
        });

        String expectedMessage = "Locality is not found for ID: " + localityId;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void updateAttraction_ShouldReturnUpdatedAttraction_WhenAttractionExists() {
        Long attractionId = 1L;
        AttractionDTO attractionDTO = new AttractionDTO();
        attractionDTO.setId(attractionId);
        attractionDTO.setShortDescription("Updated description");

        AttractionEntity attractionEntity = new AttractionEntity();
        attractionEntity.setId(attractionId);
        var le = new LocalityEntity();
        le.setId(0L);
        attractionEntity.setLocality(le);
        attractionEntity.setShortDescription("Old description");
        attractionEntity.setAssistances(Collections.emptyList());

        when(attractionRepository.findById(attractionId)).thenReturn(Optional.of(attractionEntity));
        when(attractionRepository.save(attractionEntity)).thenReturn(attractionEntity);

        AttractionDTO result = attractionService.updateAttraction(attractionDTO);

        assertEquals("Updated description", result.getShortDescription());
        assertEquals(attractionId, result.getId());
    }

    @Test
    void updateAttraction_ShouldThrowException_WhenAttractionDoesNotExist() {
        Long attractionId = 1L;
        AttractionDTO attractionDTO = new AttractionDTO();
        attractionDTO.setId(attractionId);
        attractionDTO.setShortDescription("Some description");

        when(attractionRepository.findById(attractionId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            attractionService.updateAttraction(attractionDTO);
        });

        String expectedMessage = "Attraction entity is not found with ID: " + attractionId;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void deleteAttraction_ShouldCallDeleteById_WhenAttractionExists() {
        Long attractionId = 1L;
        attractionService.deleteAttraction(attractionId);
        verify(attractionRepository, times(1)).deleteById(attractionId);
    }
}
