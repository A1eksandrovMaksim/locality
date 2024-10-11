package ru.epicprojects.localities.controllers;

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

@RestController
@RequestMapping("/api/attractions")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    @GetMapping
    public Page<AttractionDTO> getAllAttractions(
            @PageableDefault(sort = "name", size=10) Pageable pageable){
        return attractionService.showAllAttractions(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AttractionDTO addAttraction(@RequestBody AttractionDTO attractionDTO)
            throws EntityIsAlreadyPresentException {
        return attractionService.addAttraction(attractionDTO);
    }

    @GetMapping("/locality/{id}")
    public List<AttractionDTO> getAllAttractionsInLocality(@PathVariable Long id){
        return attractionService.showAllAttractionsInLocality(id);
    }

    @PutMapping
    public AttractionDTO updateAttraction(@RequestBody AttractionDTO attractionDTO){
        return attractionService.updateAttraction(attractionDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteAttraction(@PathVariable Long id){
        attractionService.deleteAttraction(id);
    }
}
