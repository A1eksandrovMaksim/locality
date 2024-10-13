package ru.epicprojects.localities.validate;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import ru.epicprojects.localities.dto.AttractionDTO;
import ru.epicprojects.localities.dto.LocalityDTO;
import ru.epicprojects.localities.exceptions.InvalidFieldException;

@Slf4j
public class LocalityValidator {
    public static boolean isValid(LocalityDTO localityDTO) throws InvalidFieldException {
        isValidLocality(localityDTO);
        isValidRegion(localityDTO);
        if(localityDTO.getAttractions() != null)
            for(var attractionDTO : localityDTO.getAttractions())
                AttractionValidator.isValid(attractionDTO);
        return true;
    }

    private static  boolean isValidRegion(LocalityDTO localityDTO) throws InvalidFieldException {
        if(Strings.isBlank(localityDTO.getRegion())){
            String message = "Название региона не может быт пустым.";
            throw new InvalidFieldException(message);
        }
        return true;
    }

    private static boolean isValidLocality(LocalityDTO localityDTO) throws InvalidFieldException {
        if(Strings.isBlank(localityDTO.getLocality())){
            String message = "Имя локации не может быт пустым.";
            throw new InvalidFieldException(message);
        }
        return true;
    }
}
