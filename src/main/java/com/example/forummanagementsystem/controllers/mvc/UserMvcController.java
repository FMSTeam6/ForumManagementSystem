package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.controllers.rest.AuthenticationHelper;
import com.example.forummanagementsystem.exceptions.AuthenticationFailureException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizedOperationException;
import com.example.forummanagementsystem.mappers.UserMapper;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dto.UserDto;
import com.example.forummanagementsystem.models.dto.userDtos.UserUpdateDto;
import com.example.forummanagementsystem.models.filters.SearchUser;
import com.example.forummanagementsystem.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    @Autowired
    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }


    @GetMapping
    public String showUser(Model model) {
        model.addAttribute("users", userService.getAll(new SearchUser()));
        return "UsersView";
    }

    @GetMapping("/{userId}")
    public String showSingleUser(@PathVariable int userId, Model model) {
        try {
            model.addAttribute("user", userService.getById(userId));
            return "UserView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
        }
        return "ErrorView";
    }



    //TODO UPDATE USER

    @GetMapping("/update")
    public String updatePost(Model model, HttpSession session) {
        try {
            model.addAttribute("currentUser", authenticationHelper.tryGetUserFromSession(session));
            return "userUpdateView";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("currentUser") UserUpdateDto dto,
                             BindingResult bindingResult,
                             Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        if (bindingResult.hasErrors()) {
            return "userUpdateView";
        }

        try {
            user = userMapper.fromUserUpdateDto(user.getId(), dto);
            userService.update(user);
            model.addAttribute("currentUser",user);
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("name", "duplicate_beer", e.getMessage());
            return "userUpdateView";
        }
    }

    @PostMapping("/banOrAdmin/{id}")
    public String banOrUnbanUser(@PathVariable int id, Model model,HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUserFromSession(session);
            model.addAttribute("banAdmin", userService.getById(id));
            if (user.isBanned()){
                userService.unBanUser(user);
            }
            if (user.isAdmin()){
                userService.deleteAdminRights(user);
            }
            if (!user.isAdmin()){
                userService.giveAdminRights(user);
            }
            return "UserView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


}
