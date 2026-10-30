package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    @Override
    public void addLike(Long filmId, Long userId) {
        log.info("Пользователь с id {} лайкнул фильм с id {}", userId, filmId);
        filmStorage.get(filmId).getLikes().add(filmStorage.get(userId).getId());
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        log.info("Пользователь с id {} удалил лайк у фильма с id {}", userId, filmId);
        filmStorage.get(filmId).getLikes().remove(filmStorage.get(userId).getId());
    }

    @Override
    public Collection<Film> popularFilms(Long count) {
        log.info("Популярные фильмы {}", count);
        return filmStorage.getAll().stream()
                .sorted((film1, film2) -> film1.getLikes().size() - film2.getLikes().size())
                .limit(count)
                .toList();
    }

    @Override
    public Film addFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ConditionsNotMetException("Название не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания — 200 символов");
            throw new ConditionsNotMetException("Максимальная длина описания — 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ConditionsNotMetException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма должна быть положительным числом");
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительным числом");
        }
        film.setId(filmStorage.getNextId());
        filmStorage.put(film);
        log.info("Фильм добавлен успешно");
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ConditionsNotMetException("Название не может быть пустым");
        }

        if (newFilm.getDescription().length() > 200) {
            log.error("Максимальная длина описания — 200 символов");
            throw new ConditionsNotMetException("Максимальная длина описания — 200 символов");
        }

        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза — не раньше 28 декабря 1895 года");
            throw new ConditionsNotMetException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        if (newFilm.getDuration() < 0) {
            log.error("Продолжительность фильма должна быть положительным числом");
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительным числом");
        }
        if (filmStorage.containsKey(newFilm.getId())) {
            Film oldFilm = filmStorage.get(newFilm.getId());
            if (newFilm.getName() != null) {
                oldFilm.setName(newFilm.getName());
            }
            if (newFilm.getDescription() != null) {
                oldFilm.setDescription(newFilm.getDescription());
            }
            if (newFilm.getDuration() != null) {
                oldFilm.setDuration(newFilm.getDuration());
            }
            if (newFilm.getReleaseDate() != null) {
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            }
            log.info("Фильм обновлен успешно");
            return oldFilm;
        }
        throw new NotFoundException(String.format("Фильм с id = %d не найден", newFilm.getId()));
    }

    @Override
    public Collection<Film> findAll() {
        return filmStorage.getAll();
    }
}
