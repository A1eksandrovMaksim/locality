package ru.epicprojects.localities.dao;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.epicprojects.localities.dto.AssistanceDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Entity
public class AssistanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private AssistanceDTO.Type type;
    private String shortDescription;
    @Column(nullable = false)
    private String executor;

    @ManyToMany(mappedBy = "assistances")
    private List<AttractionEntity> attractions;


}
