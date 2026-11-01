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
    private static final String FRIENDS_PATH = "/{id}/friends/{friendId}";

    private final UserService userService;

    @PutMapping(FRIENDS_PATH)
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(FRIENDS_PATH)
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> usersFriends(@PathVariable Long id) {
        return userService.usersFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> mutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
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
