package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

//@Query("UPDATE users as u SET u.email = ?3, u.name = ?2 WHERE u.id = ?1")
//void updateUser(Long id,String name,String email);
}
