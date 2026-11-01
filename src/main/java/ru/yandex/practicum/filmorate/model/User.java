package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    Long id;
    @Email
    String email;
    String login;
    String name;
    LocalDate birthday;
    @Builder.Default
    Set<Long> friends = new HashSet<>();
}
