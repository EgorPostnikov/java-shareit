package ru.practicum.shareit;

public class ErrorResponse extends Error {
    public ErrorResponse() {
    }

    public ErrorResponse(final String error) {
        super(error);
    }
}
