package com.homework.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Schema(description = "Данные пользователя")
public class UserResponseDto extends RepresentationModel<UserResponseDto> {

    @Schema(description = "Id пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Михаил")
    private String name;

    @Schema(description = "Email пользователя", example = "mikhail0096@gmail.com")
    private String email;

    @Schema(description = "Возраст пользователя", example = "29")
    private int age;

    @Schema(description = "Дата создания пользователя")
    private LocalDateTime createAt;

    public UserResponseDto() {
    }

    public UserResponseDto(Long id, String name, String email, int age, LocalDateTime createAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createAt = createAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}
