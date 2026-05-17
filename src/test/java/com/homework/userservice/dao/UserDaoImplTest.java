package com.homework.userservice.dao;

import com.homework.userservice.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDaoImplTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

    static SessionFactory sessionFactory;

    private UserDao userDao;

    private User testUser;

    @BeforeAll
    static void beforeAll() {
        String url = postgreSQLContainer.getJdbcUrl();
        String userName = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();

        sessionFactory = new Configuration()
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", url)
                .setProperty("hibernate.connection.username", userName)
                .setProperty("hibernate.connection.password", password)
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.format_sql", "true")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();

    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(sessionFactory);
        testUser = new User("Михаил", "mikhail0096@gmail.com", 29);
    }

    @AfterEach
    void tearDown() {
        List<User> users = userDao.findAll();
        for (User user : users) {
            userDao.delete(user.getId());
        }
    }

    @AfterAll
    static void closeAfterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    @DisplayName("Сохранение User. Должен сохранить User и присвоить ему ID.")
    void save_shouldPersistUser() {
        userDao.save(testUser);

        Optional<User> result = userDao.findById(testUser.getId());

        assertTrue(result.isPresent());
        assertEquals("Михаил",result.get().getName());
        assertEquals("mikhail0096@gmail.com",result.get().getEmail());
        assertEquals(29, result.get().getAge());
    }

    @Test
    @DisplayName("Поиск User по ID. Возвращает User если он существует.")
    void findById_whenUserExists_shouldReturnUser() {
        userDao.save(testUser);

        Optional<User> result = userDao.findById(testUser.getId());

        assertTrue(result.isPresent());
        assertEquals("Михаил",result.get().getName());
        assertEquals("mikhail0096@gmail.com",result.get().getEmail());
        assertEquals(29, result.get().getAge());
    }

    @Test
    @DisplayName("Поиск User по ID. Возвращает пустой Optional если User не существует.")
    void findById_whenUserNotExists_shouldReturnEmpty() {

        Optional<User> result = userDao.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Получение всех Users. Возвращает список всех Users.")
    void findAll_shouldReturnAllUsers() {
        userDao.save(testUser);
        userDao.save(new User ("Мария", "maria@gmail.com", 28));
        userDao.save(new User ("Андрей", "andrei007@gmail.com", 32));
        userDao.save(new User ("Анна", "anna@mail.ru", 31));

        List<User> users = userDao.findAll();

        assertEquals(4, users.size());
    }

    @Test
    @DisplayName("Получение всех Users. Возвращает пустой список если Users нет.")
    void findAll_whenNoUsers_shouldReturnEmptyList() {
        List<User> users = userDao.findAll();

        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("Обновление User. Должен обновить данные User в базе.")
    void update_shouldUpdateUser() {
        userDao.save(testUser);

        testUser.setName("Мария");
        testUser.setEmail("maria@gmail.com");
        testUser.setAge(28);

        userDao.update(testUser);

        Optional<User> result = userDao.findById(testUser.getId());
        assertTrue(result.isPresent());
        assertEquals("Мария", result.get().getName());
        assertEquals("maria@gmail.com", result.get().getEmail());
        assertEquals(28, result.get().getAge());
    }

    @Test
    @DisplayName("Удаление User. Должен удалить User из базы.")
    void delete_shouldRemoveUser() {
        userDao.save(testUser);
        Long id = testUser.getId();

        userDao.delete(id);

        Optional<User> result = userDao.findById(id);
        assertTrue(result.isEmpty());
    }


    @Test
    @DisplayName("Удаление User. Ничего не делает если User не существует.")
    void delete_whenUserNotExists_shouldDoNothing() {

        assertDoesNotThrow(() -> userDao.delete(99L));
    }
}
