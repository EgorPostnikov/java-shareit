package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class UserDto {
    private Long id;
    private String name;
    @NotNull(message = "Email is required.", groups = {Create.class})
    @Email(message = "Invalid email.", groups = {Create.class, Update.class})
    //@UsedEmailValidation(groups = {Create.class})
    private String email;
}
