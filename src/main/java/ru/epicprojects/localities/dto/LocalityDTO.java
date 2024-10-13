package ru.epicprojects.localities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Объект передачи данных локации.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalityDTO {

    /**
     * Название локации.
     */
    private String locality;

    /**
     * Регион, к которому относится локация.
     */
    private String region;

    /**
     * Достопримечательности, которые здесь находятся
     */
    private List<AttractionDTO> attractions;
}
