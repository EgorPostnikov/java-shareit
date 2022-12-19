package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
        private final UserService userService;

        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public List<User> get(@RequestHeader("X-Later-User-Id") long userId) {
            return userService.getItems(userId);
        }

        @PostMapping
        @ResponseStatus(HttpStatus.OK)
        public User addUser(@RequestHeader("X-Later-User-Id") Long userId,
                        @RequestBody User item) {
            return userService.addNewItem(userId, item);
        }

        @DeleteMapping("/{itemId}")
        @ResponseStatus(HttpStatus.OK)
        public void deleteUser(@RequestHeader("X-Later-User-Id") long userId,
                               @PathVariable long itemId) {
            userService.deleteItem(userId, itemId);
        }
}
