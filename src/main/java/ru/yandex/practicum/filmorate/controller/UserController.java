package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@RequestParam Long id, @RequestParam Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@RequestParam Long id, @RequestParam Long friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> usersFriends(@RequestParam Long id) {
        return userService.usersFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> mutualFriends(@RequestParam Long id, @RequestParam Long otherId) {
        return userService.mutualFriends(id, otherId);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }


    @PutMapping
    public User update(@RequestBody User newUser) {
        return userService.update(newUser);
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }
}
