package com.homework.userservice.controller;

import com.homework.userservice.dto.UserRequestDto;
import com.homework.userservice.dto.UserResponseDto;
import com.homework.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Управление пользователями")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Создать пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь создан"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные",
                    content = @Content(schema = @Schema(example = "{\"name\": \"Имя не может быть пустым.\"}"))
            )
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto request) {
        UserResponseDto created = userService.createUser(request);
        created.add(linkTo(methodOn(UserController.class).getUserById(created.getId())).withSelfRel());
        created.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all"));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Пользователь не найден: 1\"}"))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable long id) {
        UserResponseDto user = userService.findUserById(id);
        user.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        user.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all"));
        user.add(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("update"));
        user.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Получить всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей")
    @GetMapping
    public ResponseEntity<CollectionModel<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.findAllUsers();
        users.forEach(user -> {
            user.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
            user.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all"));
            user.add(linkTo(methodOn(UserController.class).updateUser(user.getId(), null)).withRel("update"));
            user.add(linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete"));
        });
        CollectionModel<UserResponseDto> result = CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Обновить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Пользователь не найден: 1\"}"))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные",
                    content = @Content(schema = @Schema(example = "{\"name\": \"Имя не может быть пустым.\"}"))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable long id,
                                                      @Valid @RequestBody UserRequestDto request) {
        UserResponseDto updated = userService.updateUser(id, request);
        updated.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        updated.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all"));
        updated.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Удалить пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь удалён"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Пользователь не найден: 1\"}"))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
