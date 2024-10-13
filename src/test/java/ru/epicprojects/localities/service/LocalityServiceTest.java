package ru.epicprojects.localities.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.epicprojects.localities.dao.LocalityEntity;
import ru.epicprojects.localities.dto.LocalityDTO;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;
import ru.epicprojects.localities.exceptions.InvalidFieldException;
import ru.epicprojects.localities.repositories.AttractionRepository;
import ru.epicprojects.localities.repositories.LocalityRepository;

public class LocalityServiceTest {

    @Mock
    private LocalityRepository localityRepository;

    @Mock
    private AttractionRepository attractionRepository;

    @InjectMocks
    private LocalityService localityService;

    private LocalityDTO localityDTO;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        localityDTO = new LocalityDTO();
        localityDTO.setLocality("locality");
        localityDTO.setRegion("region");
        localityDTO.setAttractions(Collections.emptyList());
    }

    @Test
    void addLocality_ShouldThrowException_WhenLocalityExists(){
        when(localityRepository.findByLocalityAndRegion(anyString(), anyString()))
                .thenReturn(Optional.of(new LocalityEntity()));

        EntityIsAlreadyPresentException exception =
                assertThrows(EntityIsAlreadyPresentException.class, ()-> localityService.addLocality(localityDTO));

        assertEquals("Can't insert. Locality with name:locality in region:region already existing.", exception.getMessage());
        verify(localityRepository, times(1)).findByLocalityAndRegion(anyString(), anyString());
        verify(localityRepository, never()).save(any());
    }

    @Test
    void addLocality_ShouldReturnLocalityDTO_WhenSuccessful()
            throws EntityIsAlreadyPresentException, InvalidFieldException {

        when(localityRepository.findByLocalityAndRegion(anyString(), anyString())).thenReturn(Optional.empty());
        when(attractionRepository.findAllById(any(List.class))).thenReturn(Collections.emptyList());
        when(localityRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LocalityDTO result = localityService.addLocality(localityDTO);

        assertNotNull(result);
        verify(localityRepository, times(1)).save(any());
    }
}
