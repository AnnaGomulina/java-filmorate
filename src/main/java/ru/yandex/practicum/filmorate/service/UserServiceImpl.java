package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public void addFriend(Long id1, Long id2) {
        log.info("Пользователь с id {} добавил друга с id {}", id1, id2);
        userStorage.get(id1).getFriends().add(id2);
        userStorage.get(id2).getFriends().add(id1);
    }

    @Override
    public void removeFriend(Long id1, Long id2) {
        log.info("Пользователь с id {} удалил друга с id {}", id1, id2);
        userStorage.get(id1).getFriends().remove(id2);
        userStorage.get(id2).getFriends().remove(id1);
    }

    @Override
    public Collection<User> mutualFriends(Long id1, Long id2) {
        log.info("Общие друзья пользователя с id {} с пользователем с id {}", id1, id2);
        Collection<User> mutualFriends = new ArrayList<>();
        for (Long id : userStorage.get(id1).getFriends()) {
            if (userStorage.get(id2).getFriends().contains(id)) {
                mutualFriends.add(userStorage.get(id));
            }
        }
        return mutualFriends;
    }

    @Override
    public User addUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Электронная почта не может быть пустой и должна содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ConditionsNotMetException("Логин не может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
        }
        user.setId(userStorage.getNextId());
        userStorage.put(user);
        return user;
    }

    @Override
    public User update(User newUser) {
        if (newUser.getEmail() == null || newUser.getEmail().isBlank() || !newUser.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (newUser.getLogin() == null || newUser.getLogin().isBlank() || newUser.getLogin().contains(" ")) {
            throw new ConditionsNotMetException("Логин не может быть пустым и содержать пробелы");
        }

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
        }
        if (userStorage.containsKey(newUser.getId())) {
            User oldUser = userStorage.get(newUser.getId());
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
        throw new NotFoundException(String.format("Пользователь с id = %d не найден", newUser.getId()));
    }

    @Override
    public Collection<User> findAll() {
        return userStorage.getAll();
    }

    @Override
    public Collection<User> usersFriends(Long id) {
        return userStorage.get(id).getFriends().stream().map(id1 -> userStorage.get(id1)).toList();
    }
}
