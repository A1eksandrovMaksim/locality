package ru.epicprojects.localities.validate;


import org.apache.logging.log4j.util.Strings;
import ru.epicprojects.localities.dto.AssistanceDTO;
import ru.epicprojects.localities.dto.AttractionDTO;
import ru.epicprojects.localities.exceptions.InvalidFieldException;

public class AssistanceValidator {
    public static boolean isValid(AssistanceDTO assistanceDTO) throws InvalidFieldException {
        isValidType(assistanceDTO);
        isValidExecutor(assistanceDTO);
        return true;
    }

    private static  boolean isValidType(AssistanceDTO assistanceDTO) throws InvalidFieldException {
        if(assistanceDTO.getType() == null){
            String message = "Не указан тип услуги.";
            throw new InvalidFieldException(message);
        }
        return true;
    }

    private static  boolean isValidExecutor(AssistanceDTO assistanceDTO) throws InvalidFieldException {
        if(Strings.isBlank(assistanceDTO.getExecutor())){
            String message = "Не указан исполнитель услуги";
            throw new InvalidFieldException(message);
        }
        return true;
    }
}
