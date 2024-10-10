package ru.epicprojects.localities.exceptions;

public class EntityIsAlreadyPresentException extends Exception {
    public EntityIsAlreadyPresentException(String message) {
        super(message);
    }
}
