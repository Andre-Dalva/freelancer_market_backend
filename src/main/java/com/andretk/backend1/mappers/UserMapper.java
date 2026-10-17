package com.andretk.backend1.mappers;

import com.andretk.backend1.dtos.CompleteUserDto;
import com.andretk.backend1.dtos.RegisterUserRequest;
import com.andretk.backend1.dtos.UpdateUserRequest;
import com.andretk.backend1.dtos.UserDto;
import com.andretk.backend1.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    CompleteUserDto toCompleteUserDto(User user);

    @Mapping(target = "passwordHash", ignore = true) //Ignores the raw password
    User toUser(RegisterUserRequest request);

    void update(UpdateUserRequest request, @MappingTarget User user);
}
