package com.homework.userservice;

import com.homework.userservice.dao.UserDao;
import com.homework.userservice.dao.UserDaoImpl;
import com.homework.userservice.service.UserService;
import com.homework.userservice.service.UserServiceImpl;
import com.homework.userservice.ui.ConsoleMenu;
import com.homework.userservice.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDao userDao = new UserDaoImpl();
    private static final UserService userService = new UserServiceImpl(userDao);

    public static void main(String[] args) {
        logger.info("Запуск приложения.");

        try {
            ConsoleMenu consoleMenu = new ConsoleMenu(scanner, userService);
            consoleMenu.start();
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
            logger.info("Приложение завершено.");
        }
    }
}
