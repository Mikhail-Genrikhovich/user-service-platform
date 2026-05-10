package com.homework.userservice.ui;

import com.homework.userservice.dao.UserDao;
import com.homework.userservice.dao.UserDaoImpl;
import com.homework.userservice.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleMenu {

    private static final int CHOICE_RANGE = 5;
    private static final Logger logger = LoggerFactory.getLogger(ConsoleMenu.class);
    private final UserDao userDao = new UserDaoImpl();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        logger.info("Запуск консольного меню.");
        boolean isStart = true;

        while (isStart) {
            printMenu();
            int choice = readChoice("Выберите действие: ");

            switch (choice) {
                case 1 -> createUser();
                case 2 -> findUserById();
                case 3 -> findAllUsers();
                case 4 -> updateUser();
                case 5 -> deleteUser();
                case 0 -> {
                    isStart = false;
                    logger.info("Завершение программы.");
                    System.out.println("Программа завершена!");
                }
                default -> System.out.println("Неизвестная ошибка...");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("          USER's DB");
        System.out.println("=".repeat(30));
        System.out.println("1. Создать пользователя.");
        System.out.println("2. Найти пользователя по ID.");
        System.out.println("3. Показать всех пользователей.");
        System.out.println("4. Изменить пользователя.");
        System.out.println("5. Удалить пользователя.");
        System.out.println("0. Выход.");
        System.out.println("=".repeat(30) + "\n");
    }

    private void createUser() {
        System.out.println("\n--- Создание пользователя ---");
        String name = readString("Введите имя пользователя:");
        String email = readEmail("Введите email пользователя: ");
        int age = readAge("Введите возраст пользователя: ");

        User user = new User(name, email, age);
        userDao.save(user);
        System.out.println("Пользователь успешно создан.");
    }

    private void findUserById() {
        System.out.println("\n--- Поиск пользователя ---");
        long id = readId("Введите ID пользователя: ");

        Optional<User> userOptional = userDao.findById(id);
        if(userOptional.isPresent()) {
            System.out.println("Пользователь найден: " + userOptional.get());
        } else {
            System.out.printf("Пользователь с ID %d не найден.\n", id);
        }

    }

    private void findAllUsers() {
        List<User> users = userDao.findAll();
        if (!users.isEmpty()){
            System.out.println("\n--- Список пользователей ---");
            users.forEach(user -> System.out.println(user));
        } else {
            System.out.println("Пользователей нет.");
        }
    }

    private void updateUser() {
        System.out.println("\n--- Обновление данных пользователя ---");
        long id = readId("Введите ID пользователя: ");

        Optional<User> existing = userDao.findById(id);

        if (existing.isEmpty()){
            System.out.printf("Пользователь с ID %d не найден.\n", id);
            return;
        }

        User user = existing.get();

        String name = readString("Введите новое имя: ");
        String email = readEmail("Введите новый email: ");
        int age = readAge("Введите новый возраст: ");

        user.setName(name);
        user.setEmail(email);
        user.setAge(age);

        userDao.update(user);
        System.out.println("Данные пользователя успешно заменены.");
    }

    private void deleteUser() {
        System.out.println("\n--- Удаление пользователя ---");
        long id = readId("Введите ID пользователя: ");

        Optional<User> existing = userDao.findById(id);

        if (existing.isEmpty()){
            System.out.printf("Пользователь с ID %d не найден.\n", id);
            return;
        }

        userDao.delete(id);
        System.out.println("Пользоветель успешно удален.");
    }

    private String readString(String prompt) {
        System.out.println(prompt);
        String string;

        while (true){
            string = scanner.nextLine().trim();
            if (!string.isBlank()){
                return string;
            } else {
                System.out.println("Поле не может быть пустым.");
            }
        }
    }

    private String readEmail(String prompt){
        while (true) {
            String email = readString(prompt);
            if (email.contains("@")) {
                return email;
            } else {
                System.out.println("Введен невалидный почтовый адрес.");
            }
        }
    }

    private int readAge(String prompt) {
        System.out.println(prompt);

        while (true) {
            try {
                int age = Integer.parseInt(scanner.nextLine().trim());
                if (age >= 0 && age < 100) {
                    return age;
                }
                System.out.println("Введен отрицательный или слишком большой возраст. Повторите ввод.");
            } catch (NumberFormatException e) {
                System.out.println("Введено невалидное значение. Повторите ввод.");
            }
        }
    }

    private long readId(String prompt) {
        System.out.println(prompt);

        while (true) {
          try {
              long id = Long.parseLong(scanner.nextLine().trim());
              if (id >= 0) {
                  return id;
              }
            System.out.println("Введен отрицательный ID. Повторите ввод.");
          } catch(NumberFormatException e) {
              System.out.println("Введено невалидное значение. Повторите ввод.");
          }
        }
    }

    private int readChoice(String prompt) {
        System.out.println(prompt);

        while (true) {
            try {
                int age = Integer.parseInt(scanner.nextLine().trim());
                if (age >= 0 && age <= CHOICE_RANGE) {
                    return age;
                }
                System.out.println("Введено отрицательное или слишком большое значение. Повторите ввод.");
            } catch (NumberFormatException e) {
                System.out.println("Введено невалидное значение. Повторите ввод.");
            }
        }
    }

}
