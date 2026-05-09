package com.homework.userservice.dao;

import com.homework.userservice.entity.User;
import com.homework.userservice.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void save(User user) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.info("Пользователь сохранен: {}", user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка при сохранении пользователя: {}", e.getMessage());
            throw new RuntimeException("Не удалось сохранить пользователя!", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            User user = session.get(User.class, id);
            if (user != null) {
                logger.info("Пользователь найден: {}", user);
            } else {
                logger.info("Пользователь c id: {} не найден.", id);
            }

            return Optional.ofNullable(user);
        } catch (Exception e) {
            logger.error("Ошибка при поиске пользователя: {}", e.getMessage());
            throw new RuntimeException("Не удалось найти пользователя", e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession();) {
        List<User> users = session.createQuery("FROM User", User.class).list();
        logger.info("Получено пользователей: {}", users.size());
        return users;
        } catch (Exception e) {
            logger.error("Ошибка при получении списка пользователей: {}", e.getMessage());
            throw new RuntimeException("Не удалось получить список пользователей.", e);
        }

    }

    @Override
    public void update(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()){
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            logger.info("Пользователь успешно обновлен: {}", user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка при обновлении пользователя: {}", e.getMessage());
            throw new RuntimeException("Не удалось обновить пользователя.", e);
        }
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                transaction.commit();
                logger.info("Пользователь успешно удален: {}", user);
            } else {
                transaction.rollback();
                logger.info("Пользователь c id: {} не найден!", id);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.info("Ошибка при удалении пользователя: {}", e.getMessage());
            throw new RuntimeException("Не удалось удалить пользователя.", e);
        }
    }
}
