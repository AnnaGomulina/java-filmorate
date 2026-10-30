package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Service
public interface UserService {
    void addFriend(Long id1, Long id2);

    void removeFriend(Long id1, Long id2);

    Collection<User> mutualFriends(Long id1, Long id2);

    User addUser(@RequestBody User user);

    User update(@RequestBody User newUser);

    Collection<User> findAll();

    Collection<User> usersFriends(Long id);
}
