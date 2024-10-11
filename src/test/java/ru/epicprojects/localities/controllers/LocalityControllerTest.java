package ru.epicprojects.localities.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.epicprojects.localities.dto.LocalityDTO;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;
import ru.epicprojects.localities.service.LocalityService;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocalityController.class)
public class LocalityControllerTest {

    @MockBean
    private LocalityService localityService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
    }

    @Test
    void addLocality_ShouldReturnConflict_WhenLocalityAlreadyExists() throws Exception {
        LocalityDTO localityDTO = new LocalityDTO();
        localityDTO.setId(1L);
        localityDTO.setRegion("Region");
        localityDTO.setLocality("Test Locality");
        localityDTO.setAttractionIds(Arrays.asList(1L, 2L));

        when(localityService.addLocality(any(LocalityDTO.class)))
                .thenThrow(new EntityIsAlreadyPresentException("Locality already exists."));

        mockMvc.perform(post("/api/localities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localityDTO)))
                .andExpect(status().isConflict()); // Проверка статуса 409 CONFLICT
    }

    @Test
    void addLocality_ShouldReturnCreatedLocality() throws Exception {
        // Создаем тестовый объект LocalityDTO
        LocalityDTO localityDTO = new LocalityDTO();
        localityDTO.setId(1L);
        localityDTO.setLocality("Test Locality");

        // Настройка поведения мока
        when(localityService.addLocality(any(LocalityDTO.class))).thenReturn(localityDTO);

        // Выполняем POST запрос
        mockMvc.perform(post("/api/localities") // Убедитесь, что используете правильный URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(localityDTO)))
                .andExpect(status().isCreated()) // Проверка статуса 201 CREATED
                .andExpect(jsonPath("$.locality").value(localityDTO.getLocality())); // Проверка имени локалитета
    }


}
