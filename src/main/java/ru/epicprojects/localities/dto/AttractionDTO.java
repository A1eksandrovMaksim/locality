package ru.epicprojects.localities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Объект передачи данных (DTO) для аттракции.
 *
 * Этот класс используется для передачи данных о различных аттракциях
 * между слоями приложения. Он включает информацию о названии аттракции,
 * дате создания, кратком описании, типе аттракции, идентификаторе локалитета
 * и списке идентификаторов связанных ассистентов.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttractionDTO {

    /**
     * Уникальный идентификатор аттракции.
     */
    private long id;

    /**
     * Название аттракции.
     */
    private String name;

    /**
     * Дата создания аттракции.
     */
    private Date createdAt;

    /**
     * Краткое описание аттракции.
     */
    private String shortDescription;

    /**
     * Тип аттракции, который может быть одним из следующих:
     * {@link Type#CASTLE}, {@link Type#PARK}, {@link Type#MUSEUM},
     * {@link Type#ARCHEOLOGICAL_SITE}, {@link Type#RESERVE}.
     */
    private Type type;

    /**
    * Идентификатор локалитета, к которому принадлежит аттракция.
    */
    private long localityId;

    /**
     * Список идентификаторов ассистентов, связанных с аттракцией.
     */
    private List<Long> assistanceIds;

    /**
     * Перечисление, представляющее типы аттракций.
     */
    public static enum Type{
        CASTLE,PARK,MUSEUM, ARCHEOLOGICAL_SITE, RESERVE
    }
}
