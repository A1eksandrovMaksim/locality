package ru.epicprojects.localities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Объект передачи данных о достопримечательности.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttractionDTO {

    /**
     * Название достопримечательности.
     */
    private String name;

    /**
     * Дата добавления достопримечательности в БД.
     */
    private Date createdAt;

    /**
     * Краткое описание достопримечательности.
     */
    private String shortDescription;

    /**
     * Тип аттракции, который может быть одним из перечисления:
     * {@link AttractionDTO.Type}
     */
    private Type type;

    /**
     * Услуги, которые здесь могут предоставлятся
     */
    private List<AssistanceDTO> assistances;

    /**
     * Перечисление, представляющее типы достопримечательностей.
     */
    public static enum Type{
        CASTLE,PARK,MUSEUM, ARCHEOLOGICAL_SITE, RESERVE, OTHER
    }
}
