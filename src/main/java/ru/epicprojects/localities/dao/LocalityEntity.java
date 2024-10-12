package ru.epicprojects.localities.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Сущность для представления локалитета в базе данных.
 *
 * Этот класс представляет собой локалитет, который имеет
 * уникальный идентификатор, название и регион. Локалитет
 * может быть связан с множеством аттракций, которые
 * относятся к нему.
 *
 * Аннотации Lombok {@link Data}, {@link AllArgsConstructor}
 * и {@link NoArgsConstructor} используются для автоматической
 * генерации геттеров, сеттеров и конструкторов.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PUBLIC, force = true)
public class LocalityEntity {

    /**
     * Уникальный идентификатор локалитета.
     * Генерируется автоматически при добавлении в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название локалитета.
     * Это поле обязательно для заполнения.
     */
    @Column(nullable = false)
    private String locality;

    /**
     * Регион, к которому относится локалитет.
     * Это поле обязательно для заполнения.
     */
    @Column(nullable = false)
    private String region;

    /**
     * Список аттракций, связанных с локалитетом.
     * Отношение задано как OneToMany, где локалитет
     * является владельцем отношения.
     */
    @OneToMany(mappedBy = "locality")
    private List<AttractionEntity> attractions;
}
