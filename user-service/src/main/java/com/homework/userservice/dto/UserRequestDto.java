package com.homework.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Данные для создания или обновления пользователя")
public class UserRequestDto {

    @Schema(description = "Имя пользователя", example = "Михаил")
    @NotBlank(message = "Имя не может быть пустым.")
    private String name;

    @Schema(description = "Email пользователя", example = "mikhail0096@gmail.com")
    @NotBlank(message = "Email не может быть пустым.")
    @Email(message = "Некорректный формат email.")
    private String email;

    @Schema(description = "Возраст пользователя", example = "29")
    @NotNull(message = "Возраст не может быть пустым")
    @Min(value = 1, message = "Возраст должен быть больше 0")
    @Max(value = 100, message = "Возраст не должен быть больше 100")
    private Integer age;

    public UserRequestDto() {
    }

    public UserRequestDto(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
