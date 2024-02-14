package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserMvcController {

    private final UserService userService;

    @Autowired
    public UserMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUser(Model model) {
        model.addAttribute("users", userService.getAll());
        return "UsersView";
    }

    @GetMapping("/{userId}")
    public String showSingleUser(@PathVariable int userId, Model model) {
        try {
            model.addAttribute("user", userService.getById(userId));
            return "UserView";
        }catch (EntityNotFoundException e){
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error",e.getMessage());        }
            return "ErrorView";
    }

}
