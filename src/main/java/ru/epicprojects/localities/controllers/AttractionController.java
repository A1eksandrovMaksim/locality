package ru.epicprojects.localities.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.epicprojects.localities.dto.AttractionDTO;
import ru.epicprojects.localities.dto.LocalityDTO;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;
import ru.epicprojects.localities.service.AttractionService;

import java.util.List;

/**
 * Контроллер для управления аттракциями.
 *
 * Этот контроллер предоставляет API для выполнения операций
 * над аттракциями, включая получение всех аттракций,
 * добавление новой аттракции, обновление существующей,
 * получение аттракций по локалитету и удаление аттракции.
 */
@Slf4j
@RestController
@RequestMapping("/api/attractions")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    /**
     * Получить все аттракции с возможностью постраничной навигации.
     *
     * @param pageable Информация для постраничного отображения.
     * @return Page<AttractionDTO> Список аттракций с пагинацией.
     */
    @GetMapping
    public Page<AttractionDTO> getAllAttractions(
            @PageableDefault(sort = "name", size=10) Pageable pageable){
        log.info("Get all attraction with pagging: {}", pageable);
        return attractionService.showAllAttractions(pageable);
    }

    /**
     * Добавить новую аттракцию.
     *
     * @param attractionDTO Данные о новой аттракции.
     * @return AttractionDTO Добавленная аттракция.
     * @throws EntityIsAlreadyPresentException Если аттракция уже существует.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AttractionDTO addAttraction(@RequestBody AttractionDTO attractionDTO)
            throws EntityIsAlreadyPresentException {
        log.info("Adding attraction: {}", attractionDTO);
        return attractionService.addAttraction(attractionDTO);
    }

    /**
     * Получить все аттракции в указанном локалитете.
     *
     * @param id Идентификатор локалитета.
     * @return List<AttractionDTO> Список аттракций в локалитете.
     */
    @GetMapping("/locality/{id}")
    public List<AttractionDTO> getAllAttractionsInLocality(@PathVariable Long id){
        log.info("Get attractions in locality with ID: {}", id);
        return attractionService.showAllAttractionsInLocality(id);
    }

    /**
     * Обновить существующую аттракцию.
     *
     * @param attractionDTO Данные для обновленной аттракции.
     * @return AttractionDTO Обновленная аттракция.
     */
    @PutMapping
    public AttractionDTO updateAttraction(@RequestBody AttractionDTO attractionDTO){
        log.info("Updating attraction: {}", attractionDTO);
        return attractionService.updateAttraction(attractionDTO);
    }

    /**
     * Удалить аттракцию по ее идентификатору.
     *
     * @param id Идентификатор аттракции для удаления.
     */
    @DeleteMapping("/{id}")
    public void deleteAttraction(@PathVariable Long id){
        log.info("Deleting attraction with ID: {}", id);
        attractionService.deleteAttraction(id);
    }
}
