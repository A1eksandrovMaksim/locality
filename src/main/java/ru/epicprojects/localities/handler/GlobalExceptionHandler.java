package ru.epicprojects.localities.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityIsAlreadyPresentException.class)
    public ResponseEntity<String> handleEntityIsAlreadyPresentException(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e){
        return new ResponseEntity<>("An error occured: " +
                e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
