package com.homework.userservice.ui;

import com.homework.userservice.entity.User;
import com.homework.userservice.service.UserService;
import com.homework.userservice.util.UserValidator;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public enum MenuOption {

    CREATE_USER(Constant.CREATE_USER) {
        @Override
        public void execute(Scanner scanner, UserService userService) {
            System.out.println("\n--- Создание пользователя ---");
            String name = UserValidator.readString("Введите имя пользователя:", scanner);
            String email = UserValidator.readEmail("Введите email пользователя: ", scanner);
            int age = UserValidator.readAge("Введите возраст пользователя: ", scanner);

            userService.createUser(name,email,age);
            System.out.println("Пользователь успешно создан.");
        }
    },

    FIND_USER_BY_ID(Constant.FIND_USER_BY_ID) {
        @Override
        public void execute(Scanner scanner, UserService userService) {
            System.out.println("\n--- Поиск пользователя ---");
            long id = UserValidator.readId("Введите ID пользователя: ", scanner);

            Optional<User> userOptional = userService.findUserById(id);

            if(userOptional.isPresent()) {
                System.out.println("Пользователь найден: " + userOptional.get());
            } else {
                System.out.printf("Пользователь с ID %d не найден.\n", id);
            }
        }
    },

    FIND_ALL_USERS(Constant.FIND_ALL_USERS) {
        @Override
        public void execute(Scanner scanner, UserService userService) {
            List<User> users = userService.findAllUsers();

            if (!users.isEmpty()){
                System.out.println("\n--- Список пользователей ---");
                users.forEach(user -> System.out.println(user));
            } else {
                System.out.println("Пользователей нет.");
            }
        }
    },

    UPDATE_USER(Constant.UPDATE_USER) {
        @Override
        public void execute(Scanner scanner, UserService userService) {
            System.out.println("\n--- Обновление данных пользователя ---");
            long id = UserValidator.readId("Введите ID пользователя: ", scanner);
            String name = UserValidator.readString("Введите новое имя пользователя:", scanner);
            String email = UserValidator.readEmail("Введите новый email пользователя: ", scanner);
            int age = UserValidator.readAge("Введите новый возраст пользователя: ", scanner);

            if (userService.updateUser(id, name, email, age)) {
                System.out.println("Данные пользователя успешно заменены.");
            } else {
                System.out.println("Пользователь не найден.");
            }
        }
    },

    DELETE_USER(Constant.DELETE_USER) {
        @Override
        public void execute(Scanner scanner, UserService userService) {
            System.out.println("\n--- Удаление пользователя ---");
            long id = UserValidator.readId("Введите ID пользователя: ", scanner);

            if (userService.deleteUser(id)) {
                System.out.println("Пользоветель успешно удален.");
            } else {
                System.out.println("Пользователь не найден.");
            }
        }
    },

    EXIT(Constant.EXIT) {
        @Override
        public void execute(Scanner scanner, UserService userService) {
        }
    };

    private String title;

    MenuOption(String title) {
        this.title = title;
    }

    String getTitle(){
        return title;
    }

    public abstract void execute(Scanner scanner, UserService userService);

    public static MenuOption fromNumber(int number) {
        return values()[number - 1];
    }
}
