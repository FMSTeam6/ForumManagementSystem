package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.controllers.rest.AuthenticationHelper;
import com.example.forummanagementsystem.exceptions.AuthenticationFailureException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.mappers.UserMapper;
import com.example.forummanagementsystem.models.dto.UserDto;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dto.userDtos.LoginDto;
import com.example.forummanagementsystem.models.dto.userDtos.RegisterDto;
import com.example.forummanagementsystem.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    private static final String PASSWORD_CONFIRM_NOT_MATCH = "Password confirm must be match password";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    public AuthenticationMvcController(UserService userService,
                                       AuthenticationHelper authenticationHelper,
                                       UserMapper userMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "loginView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto loginDto,
                              BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "loginView";
        }

        try {
           User user =  authenticationHelper.tryAuthenticateUser(loginDto);
            session.setAttribute("currentUser",loginDto.getUsername());
            session.setAttribute("isAdmin",user.isAdmin());
            session.setAttribute("isBanned",user.isBanned());
            //TODO Where redirect user after login
            return "redirect:/auth/page";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("username", "auth_error",e.getMessage());
            return "loginView";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session){
        session.removeAttribute("currentUser");
        //TODO Where redirect user after logout
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model){
        model.addAttribute("register",new RegisterDto());
        return "registerView";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto registerDto,
                                 BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "registerView";
        }

        if (!registerDto.getPassword().equals(registerDto.getPasswordConfirm())){
            bindingResult.rejectValue("password","register_error", PASSWORD_CONFIRM_NOT_MATCH);
            return "registerView";
        }
            User user = userMapper.fromRegisterDto(registerDto);
        try {
            userService.create(user);
            return "redirect:/auth/login";
        }catch (EntityDuplicateException e){
            bindingResult.rejectValue("username","username_error",e.getMessage());
            return "registerView";
        }

    }

    @GetMapping("/page")
    public String userViewPage(Model model){
        model.addAttribute("user",new UserDto());
        return "userPageView";
    }
}
