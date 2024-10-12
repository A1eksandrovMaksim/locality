package ru.epicprojects.localities.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Сущность для представления локации в базе данных.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PUBLIC, force = true)
public class LocalityEntity {

    /**
     * Уникальный идентификатор локации.
     * Генерируется автоматически при добавлении в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название локации и регион в котором она расположена.
     * Это поле обязательно для заполнения. (Альтернативный ключ)
     */
    @Column(nullable = false)
    private LocalityCompositeAK localityCompositeAK;


    /**
     * Список достопримечательностей, связанных с локацией.
     * Отношение задано как OneToMany, где локация
     * является владельцем отношения.
     */
    @OneToMany(mappedBy = "locality")
    private List<AttractionEntity> attractions;
}
