package ru.practicum.shareit.exception;

public class ErrorResponse extends Error{
    public ErrorResponse(){}
    public ErrorResponse(final String error) {
        super(error);
    }
}
