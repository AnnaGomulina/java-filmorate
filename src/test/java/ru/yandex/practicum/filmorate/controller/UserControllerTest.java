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
        controller = new UserController();
    }

    private User createValidUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName("Test Name");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        return user;
    }

    @Test
    void addUser_shouldAddUserWithGeneratedId() {
        User user = createValidUser();

        User added = controller.addUser(user);

        assertNotNull(added.getId());
        assertEquals("test@example.com", added.getEmail());
        assertEquals("testLogin", added.getLogin());
        assertEquals("Test Name", added.getName());
        assertEquals(LocalDate.of(1990, 1, 1), added.getBirthday());

        Collection<User> all = controller.findAll();
        assertEquals(1, all.size());
        assertTrue(all.contains(added));
    }

    @Test
    void addUser_shouldGenerateUniqueIds() {
        User user1 = createValidUser();
        User user2 = createValidUser();
        user2.setEmail("second@example.com");
        user2.setLogin("secondLogin");

        User added1 = controller.addUser(user1);
        User added2 = controller.addUser(user2);

        assertNotEquals(added1.getId(), added2.getId());
        assertEquals(2, controller.findAll().size());
    }

    @Test
    void addUser_shouldThrowWhenEmailIsNull() {
        User user = createValidUser();
        user.setEmail(null);

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class,
                () -> controller.addUser(user));
        assertTrue(ex.getMessage().contains("Электронная почта"));
    }

    @Test
    void addUser_shouldThrowWhenEmailIsBlank() {
        User user = createValidUser();
        user.setEmail("   ");

        assertThrows(ConditionsNotMetException.class, () -> controller.addUser(user));
    }

    @Test
    void addUser_shouldThrowWhenEmailWithoutAt() {
        User user = createValidUser();
        user.setEmail("invalid-email");

        assertThrows(ConditionsNotMetException.class, () -> controller.addUser(user));
    }

    @Test
    void addUser_shouldThrowWhenLoginIsNull() {
        User user = createValidUser();
        user.setLogin(null);

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class,
                () -> controller.addUser(user));
        assertTrue(ex.getMessage().contains("Логин"));
    }

    @Test
    void addUser_shouldThrowWhenLoginIsBlank() {
        User user = createValidUser();
        user.setLogin("   ");

        assertThrows(ConditionsNotMetException.class, () -> controller.addUser(user));
    }

    @Test
    void addUser_shouldThrowWhenLoginContainsSpace() {
        User user = createValidUser();
        user.setLogin("test login");

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class,
                () -> controller.addUser(user));
        assertTrue(ex.getMessage().contains("пробелы"));
    }

    @Test
    void addUser_shouldThrowWhenBirthdayInFuture() {
        User user = createValidUser();
        user.setBirthday(LocalDate.now().plusDays(1));

        ConditionsNotMetException ex = assertThrows(ConditionsNotMetException.class,
                () -> controller.addUser(user));
        assertTrue(ex.getMessage().contains("Дата рождения"));
    }

    @Test
    void addUser_shouldThrowNpeWhenBirthdayIsNull() {
        User user = createValidUser();
        user.setBirthday(null);

        assertThrows(NullPointerException.class, () -> controller.addUser(user));
    }

    @Test
    void addUser_shouldNotOverrideNameWhenProvided() {
        User user = createValidUser();
        user.setName("Custom Name");

        User added = controller.addUser(user);

        assertEquals("Custom Name", added.getName());
        assertEquals("testLogin", added.getLogin());
    }

    @Test
    void update_shouldUpdateExistingUser() {
        User original = controller.addUser(createValidUser());
        Long id = original.getId();

        User update = new User();
        update.setId(id);
        update.setEmail("updated@example.com");
        update.setLogin("updatedLogin");
        update.setName("Updated Name");
        update.setBirthday(LocalDate.of(2000, 2, 2));

        User updated = controller.update(update);

        assertEquals(id, updated.getId());
        assertEquals("updated@example.com", updated.getEmail());
        assertEquals("updatedLogin", updated.getLogin());
        assertEquals("Updated Name", updated.getName());
        assertEquals(LocalDate.of(2000, 2, 2), updated.getBirthday());

        User fromStore = controller.findAll().iterator().next();
        assertEquals("updated@example.com", fromStore.getEmail());
    }

    @Test
    void update_shouldThrowWhenUserNotFound() {
        User update = createValidUser();
        update.setId(999L);

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> controller.update(update));
        assertTrue(ex.getMessage().contains("999"));
    }

    @Test
    void update_shouldThrowWhenEmailInvalid() {
        User update = createValidUser();
        update.setId(1L);
        update.setEmail("invalid");

        assertThrows(ConditionsNotMetException.class, () -> controller.update(update));
    }

    @Test
    void update_shouldThrowWhenLoginInvalid() {
        User update = createValidUser();
        update.setId(1L);
        update.setLogin("bad login");

        assertThrows(ConditionsNotMetException.class, () -> controller.update(update));
    }

    @Test
    void update_shouldThrowWhenBirthdayInFuture() {
        User update = createValidUser();
        update.setId(1L);
        update.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ConditionsNotMetException.class, () -> controller.update(update));
    }

    @Test
    void update_shouldThrowNpeWhenBirthdayIsNull() {
        User update = createValidUser();
        update.setId(1L);
        update.setBirthday(null);

        assertThrows(NullPointerException.class, () -> controller.update(update));
    }

    @Test
    void findAll_shouldReturnEmptyWhenNoUsers() {
        assertTrue(controller.findAll().isEmpty());
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        User user1 = createValidUser();
        User user2 = createValidUser();
        user2.setEmail("second@example.com");
        user2.setLogin("secondLogin");

        controller.addUser(user1);
        controller.addUser(user2);

        Collection<User> all = controller.findAll();
        assertEquals(2, all.size());
    }
}