package com.example.forummanagementsystem.mappers;

import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserMapper() {
    }


    public User fromDtoUser(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        return user;
    }
}
