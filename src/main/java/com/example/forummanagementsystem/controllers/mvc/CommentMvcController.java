package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.controllers.rest.AuthenticationHelper;
import com.example.forummanagementsystem.exceptions.AuthenticationFailureException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizedOperationException;
import com.example.forummanagementsystem.mappers.CommentMapper;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.dto.CommentDto;
import com.example.forummanagementsystem.models.dto.PostDto;
import com.example.forummanagementsystem.services.CommentServices;
import com.example.forummanagementsystem.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comment")
public class CommentMvcController {

    private final CommentServices commentServices;
    private final PostService postService;
    private final AuthenticationHelper authenticationHelper;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentMvcController(CommentServices commentServices, PostService postService, AuthenticationHelper authenticationHelper, CommentMapper commentMapper) {
        this.commentServices = commentServices;
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
        this.commentMapper = commentMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    //TODO Create Comment

//    @GetMapping("/new")
//    public String createComment(Model model, HttpSession session) {
//        try {
//            authenticationHelper.tryGetUserFromSession(session);
//        } catch (AuthenticationFailureException e) {
//            return "redirect:/auth/login";
//        }
//        model.addAttribute("comment", new CommentDto());
//        return "createCommentView";
//    }
//
//    @PostMapping("/{postId}")
//    public String createComment(@PathVariable int postId, @Valid @ModelAttribute("comment") CommentDto dto,
//                                BindingResult errors,
//                                Model model,
//                                HttpSession session) {
//
//        User user;
//        try {
//            user = authenticationHelper.tryGetUserFromSession(session);
//        } catch (AuthenticationFailureException e) {
//            return "redirect:/auth/loginView";
//        }
//
//        if (errors.hasErrors()) {
//            return "createCommentView";
//        }
//        try {
//            Comment comment = commentMapper.fromDto(dto);
//            Post post = postService.getPostById(postId);
//            commentServices.createComment(comment, post, user);
//            return "redirect:/";
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
//            model.addAttribute("error", e.getMessage());
//            return "ErrorView";
//        } catch (EntityDuplicateException e) {
//            errors.rejectValue("name", "duplicate_comment_name");
//            return "createCommentView";
//        }
//    }

    //TODO Update Comment

    @GetMapping("/update/{id}")
    public String updateComment(@PathVariable int id, Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }
        try {
            Comment comment = commentServices.getById(id);
            CommentDto dto = commentMapper.toDto(comment);
            model.addAttribute("comment", dto);
            return "updateCommentView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable int id,
                             @Valid @ModelAttribute("comment") CommentDto dto,
                             BindingResult bindingResult, Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }

        if (bindingResult.hasErrors()) {
            return "updateOrDeletePostView";
        }

        try {
            Comment comment = commentMapper.fromDto(id,dto);
            Post post = postService.getPostById(id);
            commentServices.updateComment(comment,post,user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("name", "duplicate_beer", e.getMessage());
            return "updateOrDeletePostView";
        } catch (AuthenticationFailureException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


    //TODO DELETE

    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable int id, Model model,HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        }catch (AuthenticationFailureException e){
            return "redirect:/auth/loginView";
        }

        try {
            Post post = postService.getPostById(id);
            commentServices.deleteComment(id, user,post);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }catch (AuthenticationFailureException e){
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

}
