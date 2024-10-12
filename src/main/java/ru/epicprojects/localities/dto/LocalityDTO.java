package ru.epicprojects.localities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Объект передачи данных (DTO) для локалитета.
 *
 * Этот класс используется для обмена данными о локалитетах между
 * слоями приложения. Содержит информацию о локалитете, включая его
 * идентификатор, название, регион и список идентификаторов аттракций,
 * связанных с данным локалитетом.
 *
 * Аннотации Lombok {@link Data}, {@link NoArgsConstructor},
 * {@link AllArgsConstructor} используются для автоматической генерации
 * стандартных методов, таких как геттеры, сеттеры, и конструкторы.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalityDTO {

    /**
     * Уникальный идентификатор локалитета.
     */
    private long id;

    /**
     * Название локалитета.
     */
    private String locality;

    /**
     * Регион, к которому относится локалитет.
     */
    private String region;

    /**
     * Список идентификаторов аттракций, связанных с локалитетом.
     */
    private List<Long> attractionIds;
}
