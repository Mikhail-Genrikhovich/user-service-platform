package com.homework.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.userservice.dto.UserRequestDto;
import com.homework.userservice.dto.UserResponseDto;
import com.homework.userservice.exception.UserNotFoundException;
import com.homework.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserController.class)
public class    UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private UserResponseDto userResponseDto;
    private UserRequestDto userRequestDto;

    @BeforeEach
    void setUp(){
        userRequestDto = new UserRequestDto("Михаил",
                "mikhail0096@gmail.com",
                29);

        userResponseDto = new UserResponseDto(1L,
                "Михаил",
                "mikhail0096@gmail.com",
                29,
                LocalDateTime.now());
    }

    @Test
    @DisplayName("Создание User. Должен вернуть созданный User.")
    void createUser_shouldReturnCreated() throws Exception {
        when(userService.createUser(any(UserRequestDto.class))).thenReturn(userResponseDto);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Михаил"));
    }

    @Test
    @DisplayName("Создание User с невалидными данными. Должен вернуть 400.")
    void createUser_shouldReturnBadRequest() throws Exception {
        UserRequestDto invalidRequest = new UserRequestDto("", "это не email", -1);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.age").exists());
    }

    @Test
    @DisplayName("Получение User по ID. Возвращает User если он существует.")
    void getUserById_shouldReturnUser() throws Exception {
        when(userService.findUserById(1L)).thenReturn(userResponseDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Михаил"))
                .andExpect(jsonPath("$.email").value("mikhail0096@gmail.com"));
    }

    @Test
    @DisplayName("Получение User по несуществующему ID. Должен вернуть 404.")
    void getUserById_shouldReturnNotFound() throws Exception {
        when(userService.findUserById(99L)).thenThrow(new UserNotFoundException(99L));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Пользователь не найден: 99"));
    }

    @Test
    @DisplayName("Получение всех User. Возвращает List<User>.")
    void getAllUsers_shouldReturnList() throws Exception {
        List<UserResponseDto> userResponseDtoList = List.of(
                userResponseDto,
                new UserResponseDto(2L, "Мария", "maria@gmail.com", 28, LocalDateTime.now()),
                new UserResponseDto(3L, "Андрей", "andrei007@gmail.com", 32, LocalDateTime.now()),
                new UserResponseDto(4L, "Анна", "anna@mail.ru", 31, LocalDateTime.now())
        );

        when(userService.findAllUsers()).thenReturn(userResponseDtoList);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].name").value("Михаил"));
    }

    @Test
    @DisplayName("Обновление User. Должен вернуть обновленный User.")
    void updateUser_shouldReturnUpdatedUser() throws Exception {
           when(userService.updateUser(eq(1L), any(UserRequestDto.class))).thenReturn(userResponseDto);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Михаил"))
                .andExpect(jsonPath("$.email").value("mikhail0096@gmail.com"));
    }

    @Test
    @DisplayName("Удаление User. Должен вернуть отсутсвие содержания.")
    void deleteUser_shouldReturnNoContent() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }


}
