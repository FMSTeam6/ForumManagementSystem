package com.example.forummanagementsystem.controllers;

import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizedOperationException;
import com.example.forummanagementsystem.mappers.UserMapper;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dto.UserDto;
import com.example.forummanagementsystem.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRestController {

    public static final String YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION =
            "You are not authorized to browse user information";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;


    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<User> getAll(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (!user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION);
            }
            return userService.getAll();
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.fromDtoUser(userDto);
            userService.create(user);
            return user;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public User get(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            tryAuthorize(id, headers);
            return userService.getById(id);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    private void tryAuthorize(int id, HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        if (!user.isAdmin() && user.getId() != id) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION);
        }
    }
    // TODO - Ban user, Delete user, Give admin rights to be implemented
}
