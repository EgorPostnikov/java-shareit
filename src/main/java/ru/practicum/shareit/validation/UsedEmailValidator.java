package ru.practicum.shareit.validation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsedEmailValidator implements ConstraintValidator<UsedEmailValidation, String> {

    private final UserService userService;

    @Autowired
    public UsedEmailValidator(UserService userService) {
        this.userService = userService;
    }

    public boolean isValid(String email, ConstraintValidatorContext cxt) {
        if (userService.isExistEmail(email)) {
            throw new ValidationException("this email already used");
        }
        return true;
    }
}