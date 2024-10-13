package ru.epicprojects.localities.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.epicprojects.localities.dto.LocalityDTO;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;
import ru.epicprojects.localities.exceptions.InvalidFieldException;
import ru.epicprojects.localities.service.LocalityService;

/**
 * Контроллер для управления локалитетами.
 *
 * Этот контроллер предоставляет API для выполнения операций
 * над локалитетами, включая добавление новых локалитетов
 * и обновление существующих.
 */
@Slf4j
@RestController
@RequestMapping("/api/localities")
public class LocalityController {

    @Autowired
    private LocalityService localityService;

    /**
     * Добавить новый локалитет.
     *
     * @param localityDTO Данные о новом локалитете.
     * @return LocalityDTO Добавленный локалитет.
     * @throws EntityIsAlreadyPresentException Если локалитет уже существует.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocalityDTO addLocality(@RequestBody LocalityDTO localityDTO)
            throws EntityIsAlreadyPresentException, InvalidFieldException {
        log.info("Adding locality: {}", localityDTO);
        return localityService.addLocality(localityDTO);
    }
}
