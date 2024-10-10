package ru.epicprojects.localities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttractionDTO {

    private long id;
    private String name;
    private Date createdAt;
    private String shortDescription;
    private Type type;
    private long localityId;
    private List<Long> assistanceIds;


    public static enum Type{
        CASTLE, PARK, MUSEUM, ARCHEOLOGICAL_SITE, RESERVE
    }
}
