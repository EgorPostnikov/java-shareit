package ru.practicum.shareit.gateway.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createUser(UserDto requestDto) {
        return post("", requestDto);
    }

    public ResponseEntity<Object> getUserById(long userId) {
        return get("/" + userId, userId, null);
    }

    public ResponseEntity<Object> updateUser(UserDto user) {
        return patch("/"+user.getId(), user.getId(), null, user);
    }

    public ResponseEntity<Object> deleteUser(long userId) {
        return delete("/"+userId, userId, null);
    }

    public ResponseEntity<Object> getAllUsers() {
        return get("", null, null);
    }
}
