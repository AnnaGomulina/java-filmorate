package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Service
public interface FilmService {
    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    Collection<Film> popularFilms(Long count);

    Film addFilm(@RequestBody Film film);

    Film update(@RequestBody Film newFilm);

    Collection<Film> findAll();
}
