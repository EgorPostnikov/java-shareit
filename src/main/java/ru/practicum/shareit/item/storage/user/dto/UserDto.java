package ru.practicum.shareit.item.storage.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.storage.user.validation.Create;
import ru.practicum.shareit.item.storage.user.validation.Update;
import ru.practicum.shareit.item.storage.user.validation.UsedEmailValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    @NotNull(message = "Email is required.", groups = {Create.class})
    @Email(message = "Invalid email.", groups = {Create.class, Update.class})
    @UsedEmailValidation(groups = {Create.class})
    private String email;

}
