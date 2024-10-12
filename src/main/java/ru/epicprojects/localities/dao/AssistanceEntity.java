package ru.epicprojects.localities.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epicprojects.localities.dto.AssistanceDTO;

import java.util.List;

/**
 * Сущность для представления услуги в базе данных.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Entity
public class AssistanceEntity {
    /**
     * Уникальный идентификатор услуги.
     * Генерируется автоматически при добавлении в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Тип услуги, который может быть одним из значений перечисления
     * {@link AssistanceDTO.Type}.
     */
    @Column(nullable = false)
    private AssistanceDTO.Type type;

    /**
     * Краткое описание услуги.
     */
    private String shortDescription;

    /**
     * Исполнитель, предоставляющий услугу.
     * Это поле обязательно для заполнения.
     */
    @Column(nullable = false, unique = true)
    private String executor;

    /**
     * Список достопримечательностей, с которыми связана услуга.
     * Отношение задано как ManyToMany, с теми достопримечательностями,
     * которые ссылаются на эту услугу.
     */
    @ManyToMany(mappedBy = "assistances")
    private List<AttractionEntity> attractions;
}
