package com.homework.userservice.service;

import com.homework.userservice.dto.UserRequestDto;
import com.homework.userservice.dto.UserResponseDto;
import com.homework.userservice.entity.User;
import com.homework.userservice.exception.UserNotFoundException;
import com.homework.userservice.kafka.UserEventProducer;
import com.homework.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;

    public UserServiceImpl(UserRepository userRepository, UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.userEventProducer = userEventProducer;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = new User(userRequestDto.getName(),
                userRequestDto.getEmail(),
                userRequestDto.getAge());

        User saved = userRepository.save(user);

        userEventProducer.sendEvent("CREATED", saved.getEmail());
        return toDto(saved);
    }

    @Override
    public UserResponseDto findUserById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return toDto(user);
    }

    @Override
    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUser(long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setAge(userRequestDto.getAge());

        User updated = userRepository.save(user);
        return toDto(updated);
    }

    @Override
    public void delete(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        String email = user.getEmail();

        userRepository.deleteById(id);
        userEventProducer.sendEvent("DELETED", email);
    }

    private UserResponseDto toDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setName(user.getName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setAge(user.getAge());
        userResponseDto.setCreateAt(user.getCreatedAt());
        return userResponseDto;
    }
}
