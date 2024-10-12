package ru.epicprojects.localities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Объект передачи данных (DTO) для ассистента.
 *
 * Этот класс используется для передачи данных о различных типах
 * ассистентов, связанных с аттракциями. Он включает информацию о
 * названии ассистента, его типе, кратком описании, исполнителе
 * и списке идентификаторов аттракций, к которым он относится.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssistanceDTO {

    /**
     * Уникальный идентификатор ассистента.
     */
    private long id;

    /**
     * Тип ассистента, который может быть одним из следующих:
     * {@link Type#GUIDE}, {@link Type#AUTO_EXCURSION},
     * {@link Type#NUTRITION}.
     */
    private Type type;

    /**
     * Краткое описание ассистента.
     */
    private String shortDescription;

    /**
     * Исполнитель, предоставляющий услуги ассистента.
     */
    private String executor;

    /**
     * Список идентификаторов аттракций, к которым относится ассистент.
     */
    private List<Long> anntractionIds;

    /**
     * Перечисление, представляющее типы ассистентов.
     */
    public static enum Type{
        GUIDE, AUTO_EXCURSION, NUTRITION
    }
}
