package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;
    private Film validFilm;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        validFilm = new Film();
        validFilm.setName("Valid Film");
        validFilm.setDescription("Valid description");
        validFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        validFilm.setDuration(Duration.ofMinutes(120));
    }

    @Test
    void addFilm_shouldAddFilmWithId() {
        Film added = filmController.addFilm(validFilm);
        assertNotNull(added.getId());
        assertEquals("Valid Film", added.getName());
        assertEquals("Valid description", added.getDescription());
        assertEquals(LocalDate.of(2000, 1, 1), added.getReleaseDate());
        assertEquals(Duration.ofMinutes(120), added.getDuration());
    }

    @Test
    void addFilm_shouldThrowWhenNameIsBlank() {
        validFilm.setName(" ");
        assertThrows(ConditionsNotMetException.class, () -> filmController.addFilm(validFilm));
    }

    @Test
    void addFilm_shouldThrowWhenNameIsNull() {
        validFilm.setName(null);
        assertThrows(ConditionsNotMetException.class, () -> filmController.addFilm(validFilm));
    }

    @Test
    void addFilm_shouldThrowWhenDescriptionTooLong() {
        validFilm.setDescription("a".repeat(201));
        assertThrows(ConditionsNotMetException.class, () -> filmController.addFilm(validFilm));
    }

    @Test
    void addFilm_shouldThrowWhenReleaseDateTooEarly() {
        validFilm.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ConditionsNotMetException.class, () -> filmController.addFilm(validFilm));
    }

    @Test
    void addFilm_shouldThrowWhenDurationIsNegative() {
        validFilm.setDuration(Duration.ofMinutes(-10));
        assertThrows(ConditionsNotMetException.class, () -> filmController.addFilm(validFilm));
    }

    @Test
    void update_shouldUpdateFilm() {
        Film added = filmController.addFilm(validFilm);

        Film update = new Film();
        update.setId(added.getId());
        update.setName("Updated Name");
        update.setDescription("Updated Description");
        update.setReleaseDate(LocalDate.of(2001, 1, 1));
        update.setDuration(Duration.ofMinutes(150));

        Film updated = filmController.update(update);

        assertEquals(added.getId(), updated.getId());
        assertEquals("Updated Name", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        assertEquals(LocalDate.of(2001, 1, 1), updated.getReleaseDate());
        assertEquals(Duration.ofMinutes(150), updated.getDuration());
    }

    @Test
    void update_shouldThrowNotFoundWhenFilmDoesNotExist() {
        Film update = new Film();
        update.setId(999L);
        update.setName("Name");
        update.setDescription("Desc");
        update.setReleaseDate(LocalDate.now());
        update.setDuration(Duration.ofMinutes(10));

        assertThrows(NotFoundException.class, () -> filmController.update(update));
    }

    @Test
    void update_shouldThrowWhenNameIsBlank() {
        Film added = filmController.addFilm(validFilm);

        Film update = new Film();
        update.setId(added.getId());
        update.setName(" ");
        update.setDescription("Desc");
        update.setReleaseDate(LocalDate.now());
        update.setDuration(Duration.ofMinutes(10));

        assertThrows(ConditionsNotMetException.class, () -> filmController.update(update));
    }

    @Test
    void update_shouldThrowWhenDescriptionTooLong() {
        Film added = filmController.addFilm(validFilm);

        Film update = new Film();
        update.setId(added.getId());
        update.setName("Name");
        update.setDescription("a".repeat(201));
        update.setReleaseDate(LocalDate.now());
        update.setDuration(Duration.ofMinutes(10));

        assertThrows(ConditionsNotMetException.class, () -> filmController.update(update));
    }

    @Test
    void update_shouldThrowWhenReleaseDateTooEarly() {
        Film added = filmController.addFilm(validFilm);

        Film update = new Film();
        update.setId(added.getId());
        update.setName("Name");
        update.setDescription("Desc");
        update.setReleaseDate(LocalDate.of(1895, 12, 27));
        update.setDuration(Duration.ofMinutes(10));

        assertThrows(ConditionsNotMetException.class, () -> filmController.update(update));
    }

    @Test
    void update_shouldThrowWhenDurationIsNegative() {
        Film added = filmController.addFilm(validFilm);

        Film update = new Film();
        update.setId(added.getId());
        update.setName("Name");
        update.setDescription("Desc");
        update.setReleaseDate(LocalDate.now());
        update.setDuration(Duration.ofMinutes(-10));

        assertThrows(ConditionsNotMetException.class, () -> filmController.update(update));
    }

    @Test
    void findAll_shouldReturnAllFilms() {
        filmController.addFilm(validFilm);

        Film second = new Film();
        second.setName("Second Film");
        second.setDescription("Second description");
        second.setReleaseDate(LocalDate.of(2010, 1, 1));
        second.setDuration(Duration.ofMinutes(90));
        filmController.addFilm(second);

        Collection<Film> all = filmController.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void findAll_shouldReturnEmptyCollectionWhenNoFilms() {
        Collection<Film> all = filmController.findAll();
        assertTrue(all.isEmpty());
    }
}