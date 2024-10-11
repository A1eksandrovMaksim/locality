package ru.epicprojects.localities.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.epicprojects.localities.dto.LocalityDTO;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;
import ru.epicprojects.localities.service.LocalityService;

@RestController
@RequestMapping("/api/localities")
public class LocalityController {

    @Autowired
    private LocalityService localityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocalityDTO addLocality(@RequestBody LocalityDTO localityDTO)
            throws EntityIsAlreadyPresentException {
        return localityService.addLocality(localityDTO);
    }

    @PutMapping
    public LocalityDTO updateLocality(LocalityDTO localityDTO){
        return localityService.updateLocality(localityDTO);
    }
}
