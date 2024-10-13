package ru.epicprojects.localities.validate;

import org.apache.logging.log4j.util.Strings;
import ru.epicprojects.localities.dto.AttractionDTO;
import ru.epicprojects.localities.dto.LocalityDTO;
import ru.epicprojects.localities.exceptions.InvalidFieldException;

public class AttractionValidator{
    public static boolean isValid(AttractionDTO attractionDTO) throws InvalidFieldException {
        isValidName(attractionDTO);
        isValidType(attractionDTO);
        if(attractionDTO.getAssistances() != null)
            for(var assistanceDTO : attractionDTO.getAssistances())
                AssistanceValidator.isValid(assistanceDTO);
        return true;
    }

    private static  boolean isValidName(AttractionDTO attractionDTO) throws InvalidFieldException {
        if(Strings.isBlank(attractionDTO.getName())){
            String message = "Название услуги не может быт пустым.";
            throw new InvalidFieldException(message);
        }
        return true;
    }

    private static  boolean isValidType(AttractionDTO attractionDTO) throws InvalidFieldException {
        if(attractionDTO.getType() == null){
            String message = "Услуга не может не иметь типа";
            throw new InvalidFieldException(message);
        }
        return true;
    }
}
