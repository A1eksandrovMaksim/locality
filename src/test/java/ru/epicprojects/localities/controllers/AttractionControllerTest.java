package ru.epicprojects.localities.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.epicprojects.localities.dto.AttractionDTO;
import ru.epicprojects.localities.dto.LocalityDTO;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;
import ru.epicprojects.localities.service.AttractionService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AttractionController.class)
public class AttractionControllerTest {

    @MockBean
    private AttractionService attractionService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;
    private AttractionDTO attractionDTO;
    private LocalityDTO localityDTO;

    @BeforeEach
    void setUp(){
        attractionDTO = new AttractionDTO();
        attractionDTO.setName("casle");
        attractionDTO.setType(AttractionDTO.Type.CASTLE);
        attractionDTO.setCreatedAt(new Date());
        attractionDTO.setShortDescription("This is castle");

        localityDTO = new LocalityDTO("Locality", "Region", Collections.singletonList(attractionDTO));
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllAttractions_ShouldReturnPageOfAttractions() throws Exception {
        AttractionDTO attractionDTO = new AttractionDTO();
        attractionDTO.setName("Amusement Park");
        attractionDTO.setShortDescription("A fun place for family.");
        attractionDTO.setType(AttractionDTO.Type.CASTLE);

        List<AttractionDTO> attractions = new ArrayList<>();
        attractions.add(attractionDTO);

        Page<AttractionDTO> page = new PageImpl<>(attractions, PageRequest.of(0, 10), attractions.size());

        when(attractionService.showAllAttractions(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/attractions")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].name").value(attractionDTO.getName()));
    }

    @Test
    void addAttraction_ShouldReturnCreatedAttraction() throws Exception {

        when(attractionService.addAttraction(any(AttractionDTO.class), any(LocalityDTO.class))).thenAnswer(i->i.getArgument(0));

        // Выполняем POST запрос
        mockMvc.perform(post("/api/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("locality", localityDTO.getLocality())
                        .param("region", localityDTO.getRegion())
                        .content(objectMapper.writeValueAsString(attractionDTO)))
                .andExpect(status().isCreated()) // Проверка статуса 201 CREATED
                .andExpect(jsonPath("$.name").value(attractionDTO.getName())) // Проверка имени
                .andExpect(jsonPath("$.shortDescription").value(attractionDTO.getShortDescription())) // Проверка краткого описания
                .andExpect(jsonPath("$.type").value(attractionDTO.getType().toString())); // Проверка типа
    }

    @Test
    void addAttraction_ShouldThrowException_WhenAttractionAlreadyExists() throws Exception {

        when(attractionService.addAttraction(any(AttractionDTO.class), any(LocalityDTO.class)))
                .thenThrow(new EntityIsAlreadyPresentException("Attraction already exists"));

        mockMvc.perform(post("/api/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("locality", localityDTO.getLocality())
                        .param("region", localityDTO.getRegion())
                        .content(objectMapper.writeValueAsString(attractionDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void getAllAttractionsInLocality_ShouldReturnAttractionsList() throws Exception {
        AttractionDTO attraction1 = new AttractionDTO();
        attraction1.setName("Roller Coaster");
        attraction1.setShortDescription("Exciting roller coaster ride.");

        AttractionDTO attraction2 = new AttractionDTO();
        attraction2.setName("Ferris Wheel");
        attraction2.setShortDescription("A giant Ferris wheel with amazing views.");

        List<AttractionDTO> attractions = Arrays.asList(attraction1, attraction2);

        when(attractionService.showAllAttractionsInLocality(any(LocalityDTO.class))).thenReturn(attractions);

        mockMvc.perform(get("/api/attractions/in_locality")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("locality", localityDTO.getLocality())
                        .param("region", localityDTO.getRegion()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(attraction1.getName()))
                .andExpect(jsonPath("$[1].name").value(attraction2.getName()));
    }

    @Test
    void getAllAttractionsInLocality_ShouldReturnEmptyList_WhenNoAttractionsFound() throws Exception {
        when(attractionService.showAllAttractionsInLocality(any(LocalityDTO.class))).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/attractions/in_locality")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("locality", localityDTO.getLocality())
                        .param("region", localityDTO.getRegion()))
                .andExpect(status().isOk()) // Проверка статуса 200 OK
                .andExpect(jsonPath("$").isArray()) // Проверка, что ответ - массив
                .andExpect(jsonPath("$").isEmpty()); // Проверка, что массив пуст
    }

    @Test
    void updateAttraction_ShouldReturnUpdatedAttraction() throws Exception {
        AttractionDTO attraction = new AttractionDTO();
        attraction.setName("Roller Coaster");
        attraction.setShortDescription("Exciting roller coaster ride.");

        when(attractionService.updateAttraction(any(AttractionDTO.class), any(LocalityDTO.class)))
                .thenReturn(attraction);

        mockMvc.perform(put("/api/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("locality", localityDTO.getLocality())
                        .param("region", localityDTO.getRegion())
                        .content(objectMapper.writeValueAsString(attraction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(attraction.getName()))
                .andExpect(jsonPath("$.shortDescription").value(attraction.getShortDescription()));
    }

    @Test
    void updateAttraction_ShouldReturnNotFound_WhenAttractionDoesNotExist() throws Exception {

        when(attractionService.updateAttraction(any(AttractionDTO.class), any(LocalityDTO.class)))
                .thenThrow(new EntityNotFoundException("Attraction not found."));

        mockMvc.perform(put("/api/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("locality", localityDTO.getLocality())
                        .param("region", localityDTO.getRegion())
                        .content(objectMapper.writeValueAsString(attractionDTO)))
                .andExpect(status().isNotFound()); // Проверка статуса 404 NOT FOUND
    }

    @Test
    void deleteAttraction_ShouldCallDeleteService() throws Exception {
        doNothing().when(attractionService).deleteAttraction(anyString(), any(LocalityDTO.class));

        mockMvc.perform(delete("/api/attractions/name")
                .param("locality", localityDTO.getLocality())
                .param("region", localityDTO.getRegion()))
                .andExpect(status().isOk()); // Проверка статуса 200 OK
    }

    @Test
    void deleteAttraction_ShouldReturnNotFound_WhenAttractionDoesNotExist() throws Exception {
        doNothing().when(attractionService).deleteAttraction(anyString(), any(LocalityDTO.class));

        doThrow(new EntityNotFoundException("Attraction not found.")).when(attractionService).deleteAttraction(anyString(), any(LocalityDTO.class));

        mockMvc.perform(delete("/api/attractions/some_name")
                .param("locality", localityDTO.getLocality())
                .param("region", localityDTO.getRegion()))
                .andExpect(status().isNotFound()); // Проверка статуса 404 NOT FOUND
    }
}
