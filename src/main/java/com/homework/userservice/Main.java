package com.homework.userservice;

import com.homework.userservice.ui.ConsoleMenu;
import com.homework.userservice.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Запуск приложения.");

        try {
            ConsoleMenu consoleMenu = new ConsoleMenu();
            consoleMenu.start();
        } finally {
            HibernateUtil.shutdown();
            logger.info("Приложение завершено.");
        }
    }
}
