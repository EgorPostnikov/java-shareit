package ru.practicum.server.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;

import java.util.Collection;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    Collection<UserDto> toUserDtos(Collection<User> users);
}
