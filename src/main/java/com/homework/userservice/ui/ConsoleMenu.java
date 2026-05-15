package com.homework.userservice.ui;

import com.homework.userservice.service.UserService;
import com.homework.userservice.service.UserServiceImpl;
import com.homework.userservice.util.UserValidator;

import java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserServiceImpl();

    public void start() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("          USER's DB");
        System.out.println("=".repeat(30));

        while (true) {
            printMenu();
            MenuOption option = getChoice();

            if (option == MenuOption.EXIT) {
                return;
            }

            option.execute(scanner, userService);
        }
    }

    private void printMenu() {
        for (MenuOption option : MenuOption.values()) {
            System.out.printf("%d. %s%n",
                    option.ordinal() + 1 ,
                    option.getTitle());
        }
    }

    private MenuOption getChoice() {
        return MenuOption.fromNumber
                (UserValidator.readChoice
                        ("Выберите действие: ",MenuOption.values().length, scanner));
    }
}