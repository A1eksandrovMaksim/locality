package ru.epicprojects.localities.exceptions;

/**
 * Исключение, выбрасывается, если кто-то пытается создать или добавить
 * сущность, которая уже присутствует в базе данных.
 *
 * Это пользовательское исключение расширяет {@link Exception} и
 * позволяет передать сообщение об ошибке, которое описывает,
 * что именно произошло.
 */
public class EntityIsAlreadyPresentException extends Exception {
    /**
     * Создает новое исключение с заданным сообщением.
     *
     * @param message сообщение, которое будет описывать причину исключения
     */
    public EntityIsAlreadyPresentException(String message) {
        super(message);
    }
}
