package ru.practicum.shareit.gateway.exception;

public class BadRequestException extends Exception {
    public BadRequestException(final String message) {
        super(message);
    }
}
