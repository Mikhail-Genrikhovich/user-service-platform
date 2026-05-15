package com.homework.userservice.service;

import com.homework.userservice.dao.UserDao;
import com.homework.userservice.dao.UserDaoImpl;
import com.homework.userservice.entity.User;


import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userDao = new UserDaoImpl();

    @Override
    public void createUser(String name, String email, int age) {
        User user = new User(name, email, age);
        userDao.save(user);
    }

    @Override
    public Optional<User> findUserById(long id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAll();
    }

    @Override
    public boolean updateUser(long id, String name, String email, int age) {
        Optional<User> existing = userDao.findById(id);

        if (existing.isEmpty()) {
            return false;
        }

        User user = existing.get();

        user.setName(name);
        user.setEmail(email);
        user.setAge(age);

        userDao.update(user);
        return true;
    }

    @Override
    public boolean deleteUser(long id) {
        Optional<User> existing = userDao.findById(id);

        if (existing.isEmpty()){
            return false;
        }

        userDao.delete(id);
        return true;
    }
}
