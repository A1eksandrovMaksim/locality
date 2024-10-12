package ru.epicprojects.localities.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epicprojects.localities.dto.AssistanceDTO;

import java.util.List;

/**
 * Сущность для представления ассистента в базе данных.
 *
 * Этот класс представляет информацию об ассистенте, который может
 * быть связан с различными аттракциями в приложении. Он хранит
 * данные о типе ассистента, его кратком описании, исполнителе,
 * а также списке аттракций, с которыми он связан.
 *
 * Аннотации Lombok {@link Data}, {@link AllArgsConstructor} и
 * {@link NoArgsConstructor} используются для автоматического
 * генерации геттеров, сеттеров и конструкторов.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Entity
public class AssistanceEntity {
    /**
     * Уникальный идентификатор ассистента.
     * Генерируется автоматически при добавлении в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Тип ассистента, который может быть одним из значений перечисления
     * {@link AssistanceDTO.Type}.
     */
    @Column(nullable = false)
    private AssistanceDTO.Type type;

    /**
     * Краткое описание ассистента.
     */
    private String shortDescription;

    /**
     * Исполнитель, предоставляющий услуги ассистента.
     * Это поле обязательно для заполнения.
     */
    @Column(nullable = false)
    private String executor;

    /**
     * Список аттракций, с которыми связан ассистент.
     * Отношение задано как ManyToMany, с теми аттракциями,
     * которые ссылаются на эту услугу.
     */
    @ManyToMany(mappedBy = "assistances")
    private List<AttractionEntity> attractions;


}
