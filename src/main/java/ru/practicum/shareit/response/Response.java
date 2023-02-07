package ru.practicum.shareit.response;

import lombok.Getter;

@Getter
public class Response {
    private String message;

    public Response(String message) {
        this.message = message;
    }

}
