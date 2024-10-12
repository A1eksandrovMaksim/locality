package ru.epicprojects.localities.handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.epicprojects.localities.exceptions.EntityIsAlreadyPresentException;

/**
 * Глобальный обработчик исключений для контроллеров.
 *
 * Этот класс перехватывает определенные исключения, возникающие в приложении,
 * и возвращает соответствующие HTTP-ответы с кодами статуса и сообщениями.
 *
 * Обрабатываемые исключения:
 * - {@link EntityIsAlreadyPresentException} - возвращает код статуса 409 (CONFLICT).
 * - {@link EntityNotFoundException} - возвращает код статуса 404 (NOT FOUND).
 * - {@link Exception} - возвращает код статуса 500 (INTERNAL SERVER ERROR) для
 *   любых других непредвиденных исключений.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработчик исключений для {@link EntityIsAlreadyPresentException}.
     *
     * @param e исключение, которое было выброшено
     * @return ответ с сообщением об ошибке и кодом статуса 409 (CONFLICT)
     */
    @ExceptionHandler(EntityIsAlreadyPresentException.class)
    public ResponseEntity<String> handleEntityIsAlreadyPresentException(Exception e){
        log.error("Entity already present: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Обработчик исключений для {@link EntityNotFoundException}.
     *
     * @param e исключение, которое было выброшено
     * @return ответ с сообщением об ошибке и кодом статуса 404 (NOT FOUND)
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(Exception e){
        log.error("Entity not found: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Обработчик для любых других исключений.
     *
     * @param e исключение, которое было выброшено
     * @return ответ с сообщением об ошибке и кодом статуса 500 (INTERNAL SERVER ERROR)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e){
        log.error("An unexpected error occurred: {}", e.getMessage());
        return new ResponseEntity<>("An error occured: " +
                e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
