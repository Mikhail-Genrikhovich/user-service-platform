package com.homework.userservice.service;

import com.homework.userservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void createUser(String name, String email, int age);
    Optional<User> findUserById(long id);
    List<User> findAllUsers();
    boolean updateUser(long id, String name, String email, int age);
    boolean deleteUser(long id);
}
