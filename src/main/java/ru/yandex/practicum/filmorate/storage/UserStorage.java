package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    void put(User user);

    long getNextId();

    boolean containsKey(Long id);

    User get(Long id);

    Collection<User> getAll();
}
