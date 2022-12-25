package ru.practicum.shareit.user.model;

import lombok.*;
import ru.practicum.shareit.user.validation.Create;
import ru.practicum.shareit.user.validation.Update;
import ru.practicum.shareit.user.validation.UsedEmailValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class User {
    private Long id;
    private String name;
    private String email;

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

