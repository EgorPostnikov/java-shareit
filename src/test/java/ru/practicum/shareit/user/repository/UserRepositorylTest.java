package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositorylTest {
    private final EntityManager em;
    private final JdbcTemplate jdbcTemplate;
    private final UserService service;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM users ");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    void saveUserTest() {
        UserDto userDto = new UserDto(null, "Пётр", "some@email1.com");
        service.createUser(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query
                .setParameter("email", userDto.getEmail())
                .getSingleResult();
        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
        assertThat(user.getName(), equalTo(userDto.getName()));

    }

    @Test
    void getUserByIdTest() {

        UserDto userDto = new UserDto(null, "Пётр", "some@email.com");
        service.createUser(userDto);
        UserDto user = service.getUserById(1);

        assertThat(user.getId(), notNullValue());
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
        assertThat(user.getName(), equalTo(userDto.getName()));

    }
}
