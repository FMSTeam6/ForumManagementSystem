package com.example.forummanagementsystem.controllers.rest;

import com.example.forummanagementsystem.controllers.rest.AuthenticationHelper;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizedOperationException;
import com.example.forummanagementsystem.mappers.UserMapper;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dto.UserDto;
import com.example.forummanagementsystem.models.filters.FilterOptions;
import com.example.forummanagementsystem.models.filters.SearchUser;
import com.example.forummanagementsystem.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    public static final String YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION =
            "You are not authorized to browse user information";
    private static final String UPDATE_USER_ERROR_MESSAGE = "Only owner of the account can update personal info.";
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
    public List<User> getAll(@RequestHeader HttpHeaders headers,
                             @RequestParam(required = false) String username,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) String firstName,
                             @RequestParam(required = false) String lastName,
                             @RequestParam(required = false) String sortBy,
                             @RequestParam(required = false) String sortOrder) {
        SearchUser searchUser = new SearchUser(username, email, firstName, lastName, sortBy, sortOrder);
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (!user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION);
            }
            return userService.getAll(searchUser);
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

    // TODO now it is working like GET request and dont update info only show it
    @PutMapping("/{id}")
    public User update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody UserDto userDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (user.getId() != id) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UPDATE_USER_ERROR_MESSAGE);
            }
            User userToUpdate = userMapper.fromDtoUser(id, userDto);
            userService.update(userToUpdate);
            return user;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
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
