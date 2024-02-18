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
    public static final String USER_ALREADY_ADMIN = "This user is already an admin!";
    private static final String UPDATE_USER_ERROR_MESSAGE = "Only owner of the account can update personal info.";
    private static final String YOU_ARE_NOT_AUTHORIZED_TO_CHANGE_USER_STATUS =
            "You are not authorized to change user status";
    public static final String BANNED_USERS_CAN_NOT_BECOME_ADMINS = "User is banned and can not become an admin";
    public static final String USER_ALREADY_BANNED = "This user is already banned";
    public static final String USER_IS_NOT_BANNED = "Can not change the status of this user";
    public static final String USER_IS_NOT_ADMIN = "Can not change the position of this user";
    public static final String ONLY_ADMINS_CAN_DELETE_USERS = "Only forum admins can delete user info. " +
            "Please contact one of the admins!";
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
    @PutMapping("/ban/{userId}")
    public void ban(@PathVariable int userId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToBan = userService.getById(userId);
            if (!user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, YOU_ARE_NOT_AUTHORIZED_TO_CHANGE_USER_STATUS);
            }
            if (userToBan.isBanned()){
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, USER_ALREADY_BANNED);
            }
            userService.banUser(userToBan);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
    @PutMapping("/unban/{userId}")
    public void unBan(@PathVariable int userId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToUnBan = userService.getById(userId);
            if (!user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, YOU_ARE_NOT_AUTHORIZED_TO_CHANGE_USER_STATUS);
            }
            if (!userToUnBan.isBanned()){
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, USER_IS_NOT_BANNED);
            }
            userService.unBanUser(userToUnBan);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
    @PutMapping("/makeadmin/{userId}")
    public void makeAdmin(@PathVariable int userId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User futureAdmin = userService.getById(userId);
            if (!user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, YOU_ARE_NOT_AUTHORIZED_TO_CHANGE_USER_STATUS);
            }
            if (futureAdmin.isAdmin()){
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, USER_ALREADY_ADMIN);
            }
            if (futureAdmin.isBanned()){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, BANNED_USERS_CAN_NOT_BECOME_ADMINS);
            }
            userService.giveAdminRights(futureAdmin);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
    @PutMapping("/takeadmin/{userId}")
    public void takeAdmin(@PathVariable int userId, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            User userToBeNotAdmin = userService.getById(userId);
            if (!user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, YOU_ARE_NOT_AUTHORIZED_TO_CHANGE_USER_STATUS);
            }
            if (!userToBeNotAdmin.isAdmin()){
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, USER_IS_NOT_ADMIN);
            }
            userService.deleteAdminRights(userToBeNotAdmin);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (!user.isAdmin()){
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ONLY_ADMINS_CAN_DELETE_USERS);
            }
            userService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private void tryAuthorize(int id, HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        if (!user.isAdmin() && user.getId() != id) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, YOU_ARE_NOT_AUTHORIZED_TO_BROWSE_USER_INFORMATION);
        }
    }
}
