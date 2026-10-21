package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    private Film createValidFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120L);
        return film;
    }

    @Test
    void addFilm_valid() {
        Film film = createValidFilm();
        Film added = filmController.addFilm(film);

        assertNotNull(added.getId());
        assertEquals(1L, added.getId());
        assertEquals("Test Film", added.getName());
        assertEquals("Test Description", added.getDescription());
        assertEquals(LocalDate.of(2000, 1, 1), added.getReleaseDate());
        assertEquals(120L, added.getDuration());

        Collection<Film> all = filmController.findAll();
        assertEquals(1, all.size());
        assertTrue(all.contains(added));
    }

    @Test
    void addFilm_incrementingIds() {
        Film first = createValidFilm();
        Film second = createValidFilm();
        second.setName("Second Film");

        Film addedFirst = filmController.addFilm(first);
        Film addedSecond = filmController.addFilm(second);

        assertEquals(1L, addedFirst.getId());
        assertEquals(2L, addedSecond.getId());
        assertEquals(2, filmController.findAll().size());
    }

    @Test
    void addFilm_nullName() {
        Film film = createValidFilm();
        film.setName(null);

        ConditionsNotMetException ex = assertThrows(
                ConditionsNotMetException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Название не может быть пустым", ex.getMessage());
    }

    @Test
    void addFilm_blankName() {
        Film film = createValidFilm();
        film.setName("   ");

        ConditionsNotMetException ex = assertThrows(
                ConditionsNotMetException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Название не может быть пустым", ex.getMessage());
    }

    @Test
    void addFilm_descriptionTooLong() {
        Film film = createValidFilm();
        film.setDescription("a".repeat(201));

        ConditionsNotMetException ex = assertThrows(
                ConditionsNotMetException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Максимальная длина описания — 200 символов", ex.getMessage());
    }

    @Test
    void addFilm_description200Chars() {
        Film film = createValidFilm();
        film.setDescription("a".repeat(200));

        assertDoesNotThrow(() -> filmController.addFilm(film));
    }

    @Test
    void addFilm_releaseDateBefore1895() {
        Film film = createValidFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        ConditionsNotMetException ex = assertThrows(
                ConditionsNotMetException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", ex.getMessage());
    }

    @Test
    void addFilm_releaseDate1895_12_28() {
        Film film = createValidFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        assertDoesNotThrow(() -> filmController.addFilm(film));
    }

    @Test
    void addFilm_negativeDuration() {
        Film film = createValidFilm();
        film.setDuration(-1L);

        ConditionsNotMetException ex = assertThrows(
                ConditionsNotMetException.class,
                () -> filmController.addFilm(film)
        );
        assertEquals("Продолжительность фильма должна быть положительным числом", ex.getMessage());
    }

    @Test
    void addFilm_zeroDuration() {
        Film film = createValidFilm();
        film.setDuration(0L);

        assertDoesNotThrow(() -> filmController.addFilm(film));
    }

    @Test
    void update_existingFilm() {
        Film original = createValidFilm();
        Film added = filmController.addFilm(original);

        Film update = new Film();
        update.setId(added.getId());
        update.setName("Updated Name");
        update.setDescription("Updated Description");
        update.setReleaseDate(LocalDate.of(2010, 5, 5));
        update.setDuration(150L);

        Film result = filmController.update(update);

        assertEquals(added.getId(), result.getId());
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(LocalDate.of(2010, 5, 5), result.getReleaseDate());
        assertEquals(150L, result.getDuration());

        Collection<Film> all = filmController.findAll();
        assertEquals(1, all.size());
        assertEquals(result, all.iterator().next());
    }

    @Test
    void update_idNotFound() {
        Film update = createValidFilm();
        update.setId(999L);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> filmController.update(update)
        );
        assertEquals("Фильм с id = 999 не найден", ex.getMessage());
    }

    @Test
    void update_invalidName() {
        Film original = createValidFilm();
        Film added = filmController.addFilm(original);

        Film update = createValidFilm();
        update.setId(added.getId());
        update.setName(null);

        assertThrows(ConditionsNotMetException.class, () -> filmController.update(update));
    }

    @Test
    void update_descriptionTooLong() {
        Film original = createValidFilm();
        Film added = filmController.addFilm(original);

        Film update = createValidFilm();
        update.setId(added.getId());
        update.setDescription("a".repeat(201));

        assertThrows(ConditionsNotMetException.class, () -> filmController.update(update));
    }

    @Test
    void update_invalidReleaseDate() {
        Film original = createValidFilm();
        Film added = filmController.addFilm(original);

        Film update = createValidFilm();
        update.setId(added.getId());
        update.setReleaseDate(LocalDate.of(1895, 12, 27));

        assertThrows(ConditionsNotMetException.class, () -> filmController.update(update));
    }

    @Test
    void update_invalidDuration() {
        Film original = createValidFilm();
        Film added = filmController.addFilm(original);

        Film update = createValidFilm();
        update.setId(added.getId());
        update.setDuration(-5L);

        assertThrows(ConditionsNotMetException.class, () -> filmController.update(update));
    }

    @Test
    void findAll_emptyInitially() {
        assertTrue(filmController.findAll().isEmpty());
    }

    @Test
    void findAll_returnsAllFilms() {
        Film first = createValidFilm();
        Film second = createValidFilm();
        second.setName("Second");

        filmController.addFilm(first);
        filmController.addFilm(second);

        Collection<Film> all = filmController.findAll();
        assertEquals(2, all.size());
    }
}