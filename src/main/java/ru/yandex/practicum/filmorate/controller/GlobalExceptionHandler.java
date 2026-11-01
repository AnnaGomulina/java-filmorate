package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConditionsNotMetException.class)
    public ResponseEntity<Map<String, String>> conditionsNotMetException(
            ConditionsNotMetException conditionsNotMetException) {
        return new ResponseEntity<>(Map.of("error", "Произошла ошибка: "
                + conditionsNotMetException.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> notFoundException(NotFoundException notFoundException) {
        return new ResponseEntity<>(Map.of("error", "Произошла ошибка: "
                + notFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> exception(Exception exception) {
        return new ResponseEntity<>(Map.of("error", "Произошла ошибка: "
                + exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

