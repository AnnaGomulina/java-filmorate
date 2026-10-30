package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    Long id;
    @NotBlank
    String name;
    String description;
    LocalDate releaseDate;
    Long duration;
    Set<Long> likes;
}
