package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

//@Query("UPDATE users u SET u.name = ?1, u.email = ?2 WHERE u.id = ?3")
 User updateUser();
}
