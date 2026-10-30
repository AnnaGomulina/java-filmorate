package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConditionsNotMetException.class)
    public ResponseEntity<String> conditionsNotMetException(ConditionsNotMetException conditionsNotMetException) {
        return new ResponseEntity<>("Произошла ошибка: " + conditionsNotMetException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundException(NotFoundException notFoundException) {
        return new ResponseEntity<>("Произошла ошибка: " + notFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception exception) {
        return new ResponseEntity<>("Произошла ошибка: " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

