package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    void put(Film film);

    long getNextId();

    boolean containsKey(Long id);

    Film get(Long id);

    Collection<Film> getAll();
}
