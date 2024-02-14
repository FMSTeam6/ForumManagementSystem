package com.example.forummanagementsystem.controllers;

import com.example.forummanagementsystem.exceptions.AuthenticationFailureException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizedOperationException;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dto.userDtos.LoginDto;
import com.example.forummanagementsystem.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticationHelper {
    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String WRONG_PASSWORD = "Wrong password!";
    private static final String WRONG_USERNAME = "Wrong username!";
    private static final String RESOURCE_REQUIRES_AUTHENTICATION = "The request resource requires authentication!";
    private static final String INVALID_AUTHENTICATION = "Wrong username or password.";
    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new UnauthorizedOperationException(RESOURCE_REQUIRES_AUTHENTICATION);
        }
        try {
            String userinfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            assert userinfo != null;
            String username = getUsername(userinfo);
            String password = getPassword(userinfo);
            User user = userService.getByUsername(username);
            if (!user.getPassword().equals(password)) {
                throw new UnauthorizedOperationException(WRONG_PASSWORD);
            }
            //  if (user.isDeleted()){
            //
            // }
            return user;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, WRONG_USERNAME);
        }
    }

    private String getUsername(String userinfo) {
        int firstSpaceIndex = userinfo.indexOf(" ");
        if (firstSpaceIndex == -1) {
            throw new UnauthorizedOperationException(WRONG_USERNAME);
        }
        return userinfo.substring(0, firstSpaceIndex);
    }

    private String getPassword(String userinfo) {
        int firstSpaceIndex = userinfo.indexOf(" ");
        if (firstSpaceIndex == -1) {
            throw new UnauthorizedOperationException(WRONG_PASSWORD);
        }
        return userinfo.substring(firstSpaceIndex + 1);
    }

    public User tryAuthenticateUser(LoginDto loginDto) {
        try {
            User user = userService.getByUsername(loginDto.getUsername());

            if (!user.getPassword().equals(loginDto.getPassword())) {
                throw new AuthenticationFailureException(INVALID_AUTHENTICATION);
            }

            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthenticationFailureException(INVALID_AUTHENTICATION);
        }
    }
}
