package ru.epicprojects.localities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Объект передачи данных услуги.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssistanceDTO {

    /**
     * Тип услуги, который может быть одним из значений перечисления
     * {@link AssistanceDTO.Type}
     */
    private Type type;

    /**
     * Краткое описание услуги.
     */
    private String shortDescription;

    /**
     * Имя исполнителя, предоставляющего услугу.
     */
    private String executor;

    /**
     * Перечисление, представляющее типы ассистентов.
     */
    public static enum Type{
        GUIDE, AUTO_EXCURSION, NUTRITION, ANIMATOR, OTHER
    }
}
