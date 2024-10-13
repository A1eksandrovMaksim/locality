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
 * Сущность для представления достопримечательности в базе данных.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Entity
public class AttractionEntity {

    /**
     * Уникальный идентификатор достопримечательности.
     * Генерируется автоматически при добавлении в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Поле, которое объединяет в себе название достопримечательности и на какой локации она находится
     * Альтернативный ключ
     */
    @EmbeddedId
    private AttractionCompositeAK attractionCompositeAK;

    @ManyToOne(optional = false)
    @MapsId("localityId")
    @JoinColumn(name = "locality_id", nullable = false)
    private LocalityEntity locality;

    /**
     * Дата добавления достопримечательности в базу данных.
     */
    @Temporal(value = TemporalType.DATE)
    @Column(updatable = false)
    private Date createdAt;

    /**
     * Краткое описание достопримечательности.
     */
    private String shortDescription;

    /**
     * Тип достопримечательности, который может быть одним из значений перечисления
     * {@link AttractionDTO.Type}.
     * Это поле обязательно для заполнения.
     */
    @Column(nullable = false)
    private AttractionDTO.Type type;

    /**
     * Список услуг, связанных с достопримечательностью.
     * Отношение задано как ManyToMany через промежуточную таблицу.
     */
    @ManyToMany
    @JoinTable(name = "attraction_assistance",
            joinColumns = @JoinColumn(name="attraction_id"),
            inverseJoinColumns = @JoinColumn(name = "assistance_id"))
    private List<AssistanceEntity> assistances;

    @PrePersist
    public void prePersist(){
        this.createdAt = new Date();
    }

}
