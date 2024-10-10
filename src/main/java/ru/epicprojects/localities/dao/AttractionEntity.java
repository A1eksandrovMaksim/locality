package ru.epicprojects.localities.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epicprojects.localities.dto.AttractionDTO;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Entity
public class AttractionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;
    @Temporal(value = TemporalType.DATE)
    @Column(nullable = false)
    private Date createdAt;
    private String shortDescription;
    @Column(nullable = false)
    private AttractionDTO.Type type;

    @ManyToOne(optional = false)
    @JoinColumn(name = "locality_id", nullable = false)
    private LocalityEntity locality;

    @ManyToMany
    @JoinTable(name = "attraction_assistance",
            joinColumns = @JoinColumn(name="attraction_id"),
            inverseJoinColumns = @JoinColumn(name = "assistance_id"))
    private List<AssistanceEntity> assistances;

}
