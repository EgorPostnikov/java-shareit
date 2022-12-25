package ru.practicum.shareit.user.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.controller.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsedEmailValidator implements ConstraintValidator<UsedEmailValidation, String> {

    private final UserService userService;

    @Autowired
    public UsedEmailValidator(UserService userService) {
        this.userService = userService;
    }

    public boolean isValid(String email, ConstraintValidatorContext cxt) {
        if (!userService.isNotExistEmail(email)){
            throw new ValidationException("this email already used");
        }
        return userService.isNotExistEmail(email);
    }
}
