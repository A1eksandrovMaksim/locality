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
import ru.epicprojects.localities.exceptions.InvalidFieldException;
import ru.epicprojects.localities.service.AttractionService;

import java.util.List;

/**
 * Контроллер для управления достопримечательностями.
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
    public AttractionDTO addAttraction(@RequestBody AttractionDTO attractionDTO,
            @RequestParam String locality,@RequestParam String region)
            throws EntityIsAlreadyPresentException, InvalidFieldException {
        log.info("Adding attraction: {}", attractionDTO);
        AttractionDTO responce = attractionService.addAttraction(attractionDTO, new LocalityDTO(locality, region, null));
        return responce;
    }

    /**
     * Получить все аттракции в указанном локалитете.
     */
    @GetMapping("/in_locality")
    public List<AttractionDTO> getAllAttractionsInLocality(
            @RequestParam String locality,@RequestParam String region) throws InvalidFieldException {
        log.info("Get attractions in locality with name:{}", locality);
        return attractionService.showAllAttractionsInLocality(new LocalityDTO(locality,region,null));
    }

    /**
     * Обновить существующую аттракцию.
     *
     * @param attractionDTO Данные для обновленной аттракции.
     * @return AttractionDTO Обновленная аттракция.
     */
    @PutMapping
    public AttractionDTO updateAttraction(@RequestBody AttractionDTO attractionDTO,
        @RequestParam String locality,@RequestParam String region)
            throws InvalidFieldException {
        log.info("Updating attraction: {}", attractionDTO);
        return attractionService.updateAttraction(attractionDTO, new LocalityDTO(locality, region, null));
    }

    /**
     * Удалить аттракцию по ее идентификатору.
     */
    @DeleteMapping("/{name}")
    public void deleteAttraction(
            @PathVariable String name,
            @RequestParam String locality,
            @RequestParam String region
    ) throws InvalidFieldException {
        log.info("Deleting attraction: {}", name);
        attractionService.deleteAttraction(name, new LocalityDTO(locality,region,null));
    }
}
