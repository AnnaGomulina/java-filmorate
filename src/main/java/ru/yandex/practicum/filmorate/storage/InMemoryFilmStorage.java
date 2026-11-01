package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public void put(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public boolean containsKey(Long id) {
        return films.containsKey(id);
    }

    @Override
    public Film get(Long id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }
}
