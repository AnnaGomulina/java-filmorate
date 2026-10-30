package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@RequestParam Long id, @RequestParam Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@RequestParam Long id, @RequestParam Long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular?count={count}")
    public Collection<Film> popular10Films(@RequestParam(defaultValue = "10") Long count) {
        return filmService.popularFilms(count);
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        return filmService.update(newFilm);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }
}
