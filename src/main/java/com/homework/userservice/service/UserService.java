package com.homework.userservice.service;

import com.homework.userservice.dto.UserRequestDto;
import com.homework.userservice.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto findUserById(long id);
    List<UserResponseDto> findAllUsers();
    UserResponseDto updateUser(long id, UserRequestDto userRequestDto);
    void delete(long id);
}

