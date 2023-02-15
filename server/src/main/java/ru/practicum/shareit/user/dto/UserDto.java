package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;


@Setter
@Getter
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    @NotNull(message = "Email is required.", groups = {Create.class})
    @Email(message = "Invalid email.", groups = {Create.class, Update.class})
    private String email;
}
