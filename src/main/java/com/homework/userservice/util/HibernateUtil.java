package com.homework.userservice.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    private static SessionFactory sessionFactory;

    private HibernateUtil(){

    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                logger.info("Создание SessionFactory.");
                sessionFactory = new Configuration()
                        .configure("hibernate.cfg.xml")
                        .buildSessionFactory();
                logger.info("SessionFactory успешно создана.");
            } catch (Exception e) {
                logger.error("Ошибка при попытке создания SessionFactory: {}",e.getMessage());
                throw new RuntimeException("Не удалось создать SessionFactory", e);
            }
        }

        return sessionFactory;
    }

    public static void shutdown(){
        if (sessionFactory == null || sessionFactory.isClosed()){
            return;
        }

        try {
            logger.info("Закрытие SessionFactory.");
            sessionFactory.close();
            logger.info("SessionFactory успешно закрыта.");
        } catch (Exception e) {
            logger.error("Ошибка при закрытии SessionFactory: {}" , e.getMessage());
        }
    }

}
