package ru.practicum.server.exception;

public class BadRequestException extends Exception {
    public BadRequestException(final String message) {
        super(message);
    }
}
