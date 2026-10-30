package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController controller;

    @BeforeEach
    void setUp() {
        controller = new UserController(null);
    }

    @Test
    void addUser_valid() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("validLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User added = controller.addUser(user);

        assertNotNull(added.getId());
        assertEquals("test@example.com", added.getEmail());
        assertEquals("validLogin", added.getLogin());
        assertEquals("Test Name", added.getName());
        assertEquals(LocalDate.of(1990, 1, 1), added.getBirthday());
        assertTrue(controller.findAll().contains(added));
    }

    @Test
    void addUser_nullNameDefaultsToLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("validLogin");
        user.setName(null);
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User added = controller.addUser(user);

        assertEquals("validLogin", added.getName());
    }

    @Test
    void addUser_blankNameDefaultsToLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("validLogin");
        user.setName("   ");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User added = controller.addUser(user);

        assertEquals("validLogin", added.getName());
    }

    @Test
    void addUser_nullEmail() {
        User user = new User();
        user.setEmail(null);
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class, () -> controller.addUser(user));
        assertTrue(ex.getMessage().contains("Электронная почта"));
    }

    @Test
    void addUser_blankEmail() {
        User user = new User();
        user.setEmail("   ");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class, () -> controller.addUser(user));
        assertTrue(ex.getMessage().contains("Электронная почта"));
    }

    @Test
    void addUser_emailWithoutAt() {
        User user = new User();
        user.setEmail("testexample.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class, () -> controller.addUser(user));
        assertTrue(ex.getMessage().contains("Электронная почта"));
    }

    @Test
    void addUser_nullLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin(null);
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class, () -> controller.addUser(user));
        assertTrue(ex.getMessage().contains("Логин"));
    }

    @Test
    void addUser_blankLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("   ");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class, () -> controller.addUser(user));
        assertTrue(ex.getMessage().contains("Логин"));
    }

    @Test
    void addUser_loginWithSpace() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("invalid login");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class, () -> controller.addUser(user));
        assertTrue(ex.getMessage().contains("Логин"));
    }

    @Test
    void addUser_futureBirthday() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now().plusDays(1));

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class, () -> controller.addUser(user));
        assertTrue(ex.getMessage().contains("Дата рождения"));
    }

    @Test
    void addUser_incrementingIds() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setBirthday(LocalDate.of(1991, 2, 2));

        User added1 = controller.addUser(user1);
        User added2 = controller.addUser(user2);

        assertEquals(1L, added1.getId());
        assertEquals(2L, added2.getId());
    }

    @Test
    void update_existingUser() {
        User original = new User();
        original.setEmail("original@example.com");
        original.setLogin("originalLogin");
        original.setName("Original Name");
        original.setBirthday(LocalDate.of(1990, 1, 1));
        User added = controller.addUser(original);

        User updated = new User();
        updated.setId(added.getId());
        updated.setEmail("updated@example.com");
        updated.setLogin("updatedLogin");
        updated.setName("Updated Name");
        updated.setBirthday(LocalDate.of(1995, 5, 5));

        User result = controller.update(updated);

        assertEquals(added.getId(), result.getId());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("updatedLogin", result.getLogin());
        assertEquals("Updated Name", result.getName());
        assertEquals(LocalDate.of(1995, 5, 5), result.getBirthday());
        assertEquals(result, controller.findAll().iterator().next());
    }

    @Test
    void update_nullNameDefaultsToLogin() {
        User original = new User();
        original.setEmail("original@example.com");
        original.setLogin("originalLogin");
        original.setName("Original Name");
        original.setBirthday(LocalDate.of(1990, 1, 1));
        User added = controller.addUser(original);

        User updated = new User();
        updated.setId(added.getId());
        updated.setEmail("updated@example.com");
        updated.setLogin("updatedLogin");
        updated.setName(null);
        updated.setBirthday(LocalDate.of(1995, 5, 5));

        User result = controller.update(updated);

        assertEquals("updatedLogin", result.getName());
    }

    @Test
    void update_invalidEmail() {
        User original = new User();
        original.setEmail("original@example.com");
        original.setLogin("originalLogin");
        original.setBirthday(LocalDate.of(1990, 1, 1));
        User added = controller.addUser(original);

        User updated = new User();
        updated.setId(added.getId());
        updated.setEmail("invalid");
        updated.setLogin("updatedLogin");
        updated.setBirthday(LocalDate.of(1995, 5, 5));

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class, () -> controller.update(updated));
        assertTrue(ex.getMessage().contains("Электронная почта"));
    }

    @Test
    void update_invalidLogin() {
        User original = new User();
        original.setEmail("original@example.com");
        original.setLogin("originalLogin");
        original.setBirthday(LocalDate.of(1990, 1, 1));
        User added = controller.addUser(original);

        User updated = new User();
        updated.setId(added.getId());
        updated.setEmail("updated@example.com");
        updated.setLogin("invalid login");
        updated.setBirthday(LocalDate.of(1995, 5, 5));

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class, () -> controller.update(updated));
        assertTrue(ex.getMessage().contains("Логин"));
    }

    @Test
    void update_futureBirthday() {
        User original = new User();
        original.setEmail("original@example.com");
        original.setLogin("originalLogin");
        original.setBirthday(LocalDate.of(1990, 1, 1));
        User added = controller.addUser(original);

        User updated = new User();
        updated.setId(added.getId());
        updated.setEmail("updated@example.com");
        updated.setLogin("updatedLogin");
        updated.setBirthday(LocalDate.now().plusDays(1));

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class, () -> controller.update(updated));
        assertTrue(ex.getMessage().contains("Дата рождения"));
    }

    @Test
    void update_idNotFound() {
        User updated = new User();
        updated.setId(999L);
        updated.setEmail("updated@example.com");
        updated.setLogin("updatedLogin");
        updated.setBirthday(LocalDate.of(1995, 5, 5));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> controller.update(updated));
        assertTrue(ex.getMessage().contains("Пользователь с id = 999 не найден"));
    }

    @Test
    void findAll_emptyInitially() {
        Collection<User> users = controller.findAll();
        assertTrue(users.isEmpty());
    }

    @Test
    void findAll_returnsAllUsers() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        controller.addUser(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setBirthday(LocalDate.of(1991, 2, 2));
        controller.addUser(user2);

        Collection<User> users = controller.findAll();
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }
}