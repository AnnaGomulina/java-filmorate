package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ConditionsNotMetException("Логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setLogin(user.getName());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getEmail() == null || newUser.getEmail().isBlank() || !newUser.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (newUser.getLogin() == null || newUser.getLogin().isBlank() || newUser.getLogin().contains(" ")) {
            throw new ConditionsNotMetException("Логин не может быть пустым и содержать пробелы");
        }

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setLogin(newUser.getName());
        }

        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() != null) {
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getName() != null) {
                oldUser.setName(newUser.getName());
            }
            if (newUser.getBirthday() != null) {
                oldUser.setBirthday(newUser.getBirthday());
            }
            if (newUser.getLogin() != null) {
                oldUser.setLogin(newUser.getLogin());
            }

            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");

    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }
}
