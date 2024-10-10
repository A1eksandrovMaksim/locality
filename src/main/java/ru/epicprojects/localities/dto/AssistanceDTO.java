package ru.epicprojects.localities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssistanceDTO {

    private long id;
    private Type type;
    private String shortDescription;
    private String executor;
    private List<Long> anntractionIds;


    public static enum Type{
        GUIDE, AUTO_EXCURSION, NUTRITION
    }
}
