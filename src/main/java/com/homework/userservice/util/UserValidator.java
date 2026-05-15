package com.homework.userservice.util;

import java.util.Scanner;

public class UserValidator {

    public static String readString(String prompt, Scanner scanner) {
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

    public static String readEmail(String prompt, Scanner scanner){
        while (true) {
            String email = readString(prompt, scanner);
            if (email.contains("@")) {
                return email;
            } else {
                System.out.println("Введен невалидный почтовый адрес.");
            }
        }
    }

    public static int readAge(String prompt, Scanner scanner) {
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

    public static long readId(String prompt, Scanner scanner) {
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

    public static int readChoice(String prompt, int range, Scanner scanner) {
        System.out.println(prompt);

        while (true) {
            try {
                int age = Integer.parseInt(scanner.nextLine().trim());
                if (age >= 0 && age <= range) {
                    return age;
                }
                System.out.println("Введено отрицательное или слишком большое значение. Повторите ввод.");
            } catch (NumberFormatException e) {
                System.out.println("Введено невалидное значение. Повторите ввод.");
            }
        }
    }
}
