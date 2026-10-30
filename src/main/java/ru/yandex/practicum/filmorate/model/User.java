package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    Long id;
    @Email
    String email;
    String login;
    String name;
    LocalDate birthday;
    Set<Long> friends;
}
