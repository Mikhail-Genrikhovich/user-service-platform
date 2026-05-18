package com.homework.userservice.service;

import com.homework.userservice.dao.UserDao;
import com.homework.userservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("Михаил", "mikhail0096@gmail.com", 29);
    }

    @Test
    @DisplayName("Создание User. Должен сохранить User.")
    void createUser_shouldCallSave() {
        String name = "Михаил";
        String email = "mikhail0096@gmail.com";
        int age = 29;

        userService.createUser(name, email, age);

        verify(userDao, times(1)).save(argThat(user ->
                user.getName().equals(name) &&
                        user.getEmail().equals(email) &&
                        user.getAge() == age
        ));
    }

    @Test
    @DisplayName("Поиск User по ID. Возвращает User, если он существует.")
    void findUserById_whenUserExists_shouldReturnUser() {
        when(userDao.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findUserById(1L);

        assertTrue(result.isPresent());

        assertEquals("Михаил", result.get().getName());
    }

    @Test
    @DisplayName("Поиск User по ID. Возвращает пустой Optional, если User не существует.")
    void findUserById_whenUserNotExists_shouldReturnEmpty() {
        when(userDao.findById(99L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Возвращения списка Users. Возвращает список Users.")
    void findAllUsers_shouldReturnListOfUsers() {
        List<User> users = List.of(
                testUser,
                new User ("Мария", "maria@gmail.com", 28),
                new User ("Андрей", "andrei007@gmail.com", 32),
                new User ("Анна", "anna@mail.ru", 31)
        );

        when(userDao.findAll()).thenReturn(users);

        List<User> result = userService.findAllUsers();

        assertEquals(4, result.size());
    }

    @Test
    @DisplayName("Возвращения списка Users. Возвращает пустой список Users.")
    void findAllUsers_whenNoUsers_shouldReturnEmptyList() {
         when(userDao.findAll()).thenReturn(List.of());

        List<User> result = userService.findAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Изменение данных у User по ID. Изменить данные если User найден и вернуть true.")
    void updateUser_whenUserExists_shouldUpdateAndReturnTrue() {
        when(userDao.findById(1L)).thenReturn(Optional.of(testUser));

        boolean result = userService.updateUser(1L, "Андрей", "andrei007@gmail.com", 32 );

        assertTrue(result);

        verify(userDao, times(1)).update(testUser);
    }

    @Test
    @DisplayName("Изменение данных у User по ID. Вернуть false если User не найден.")
    void updateUser_whenUserNotExists_shouldReturnFalse() {
        when(userDao.findById(99L)).thenReturn(Optional.empty());

        boolean result = userService.updateUser(99L, "Андрей", "andrei007@gmail.com", 32 );

        assertFalse(result);

        verify(userDao, never()).update(any());
    }

    @Test
    @DisplayName("Удаление User по ID. Удалить User если он найден и вернуть true.")
    void deleteUser_whenUserExists_shouldDeleteAndReturnTrue() {
        when(userDao.findById(1L)).thenReturn(Optional.of(testUser));

        boolean result = userService.deleteUser(1L);

        assertTrue(result);

        verify(userDao, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Удаление User по ID. Вернуть false если User не найден.")
    void deleteUser_whenUserNotExists_shouldReturnFalse() {
        when(userDao.findById(99L)).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(99L);

        assertFalse(result);

        verify(userDao, never()).delete(any());

    }

}