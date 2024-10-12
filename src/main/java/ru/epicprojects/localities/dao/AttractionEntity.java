package ru.epicprojects.localities.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epicprojects.localities.dto.AttractionDTO;

import java.util.Date;
import java.util.List;

/**
 * Сущность для представления аттракции в базе данных.
 *
 * Этот класс описывает аттракцию, включая ее название,
 * дату создания, тип, краткое описание, связь с локалитетом
 * и списком ассистентов. Аттракция может включать множество
 * ассистентов, которые предоставляют дополнительные услуги.
 *
 * Аннотации Lombok {@link Data}, {@link AllArgsConstructor}
 * и {@link NoArgsConstructor} используются для автоматической
 * генерации геттеров, сеттеров и конструкторов.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Entity
public class AttractionEntity {

    /**
     * Уникальный идентификатор аттракции.
     * Генерируется автоматически при добавлении в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Название аттракции.
     * Это поле обязательно для заполнения.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Дата создания аттракции.
     * Поле фиксируется как дата (без времени).
     * Это поле также обязательно для заполнения.
     */
    @Temporal(value = TemporalType.DATE)
    @Column(nullable = false)
    private Date createdAt;

    /**
     * Краткое описание аттракции.
     */
    private String shortDescription;

    /**
     * Тип аттракции, который может быть одним из значений перечисления
     * {@link AttractionDTO.Type}.
     * Это поле обязательно для заполнения.
     */
    @Column(nullable = false)
    private AttractionDTO.Type type;

    /**
     * Локалитет, к которому принадлежит аттракция.
     * Обязательно для заполнения (отношение ManyToOne).
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "locality_id", nullable = false)
    private LocalityEntity locality;

    /**
     * Список ассистентов, связанных с аттракцией.
     * Отношение задано как ManyToMany через промежуточную таблицу.
     */
    @ManyToMany
    @JoinTable(name = "attraction_assistance",
            joinColumns = @JoinColumn(name="attraction_id"),
            inverseJoinColumns = @JoinColumn(name = "assistance_id"))
    private List<AssistanceEntity> assistances;

}
