package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.controllers.rest.AuthenticationHelper;
import com.example.forummanagementsystem.exceptions.AuthenticationFailureException;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizedOperationException;
import com.example.forummanagementsystem.mappers.CommentMapper;
import com.example.forummanagementsystem.mappers.PostMapper;
import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dto.CommentDto;
import com.example.forummanagementsystem.models.dto.PostDto;
import com.example.forummanagementsystem.models.filters.FilterOptions;
import com.example.forummanagementsystem.services.CommentServices;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
public class PostMvcController {

    private final PostService postService;
    private final UserService userService;
    private final PostMapper postMapper;
    private final AuthenticationHelper authenticationHelper;
    private CommentMapper commentMapper;
    private CommentServices commentServices;

    @Autowired
    public PostMvcController(PostService postService,
                             UserService userService,
                             PostMapper postMapper,
                             AuthenticationHelper authenticationHelper,
                             CommentMapper commentMapper,
                             CommentServices commentServices) {
        this.postService = postService;
        this.userService = userService;
        this.postMapper = postMapper;
        this.authenticationHelper = authenticationHelper;
        this.commentMapper = commentMapper;
        this.commentServices = commentServices;
    }
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session){
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestUPI")
    public String requestURI(final HttpServletRequest request){
        return request.getRequestURI();
    }

    @GetMapping
    public String showAllPosts(Model model){
        model.addAttribute("posts",postService.get(new FilterOptions()));
        return "allPostView";
    }

    @GetMapping("/{postId}")
    public String showPost(@PathVariable int postId, Model model){
        try {
            model.addAttribute("post",postService.getPostById(postId));
            return "postView";
        }catch (EntityNotFoundException e){
            model.addAttribute("statusCode",HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error",e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/new")
    public String createNewPost(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUserFromSession(session);
        }catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }
        model.addAttribute("post", new PostDto());
        return "createPostView";
    }
    //TODO CREATE NEW POST

    @PostMapping("/new")
    public String createNewPost(@Valid @ModelAttribute("post") PostDto postDto,
                                BindingResult errors,
                                Model model, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        }catch (AuthenticationFailureException e){
            return "redirect:/auth/loginView";
        }

        if (errors.hasErrors()) {
            return "createPostView";
        }
        //TODO when is redirect after login
        try {
            Post post = postMapper.fromDto(postDto);
            postService.create(post, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error",e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e){
            errors.rejectValue("name","duplicate_post_name");
            return "createPostView";
        }
    }

    //TODO UPDATE POST

    @GetMapping("/{id}/update")
    public String updatePost(@PathVariable int id, Model model,HttpSession session ){
        try {
            authenticationHelper.tryGetUserFromSession(session);
        }catch (AuthenticationFailureException e){
            return "redirect:/auth/loginView";
        }
        try {
            Post post = postService.getPostById(id);
            PostDto dto = postMapper.toDto(post);
            model.addAttribute("postId",id);
            model.addAttribute("post",dto);
            return "updateOrDeletePostView";
        }catch (EntityNotFoundException e){
            model.addAttribute("statusCode",HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error",e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updatePost(@PathVariable int id,
                             @Valid @ModelAttribute("post") PostDto dto,
                             BindingResult bindingResult,Model model,HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        }catch (AuthenticationFailureException e){
            return "redirect:/auth/loginView";
        }

        if (bindingResult.hasErrors()) {
            return "updateOrDeletePostView";
        }

        try {
            Post post = postMapper.fromDto(id, dto);
            postService.update(post, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("name", "duplicate_beer", e.getMessage());
            return "updateOrDeletePostView";
        }catch (UnauthorizedOperationException e){
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
    //TODO DELETE

    @GetMapping("/{id}/delete")
    public String deletePost(@PathVariable int id, Model model,HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        }catch (AuthenticationFailureException e){
            return "redirect:/auth/loginView";
        }

        try {
            postService.delete(id, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }catch (UnauthorizedOperationException e){
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable int id, Model model, HttpSession session){
        try{
            User user = authenticationHelper.tryGetUserFromSession(session);
            model.addAttribute("post",postService.getPostById(id));
            postService.likePost(id,user);

            return "redirect:/posts/" + id;
        }catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (UnauthorizedOperationException e){
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/dislike")
    public String dislikePost(@PathVariable int id, Model model, HttpSession session){
        try{
            User user = authenticationHelper.tryGetUserFromSession(session);
            model.addAttribute("post",postService.getPostById(id));
            postService.dislikePost(id,user);

            return "redirect:/posts/" + id;
        }catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (UnauthorizedOperationException e){
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{postId}/comment")
    public String createComment(@PathVariable int postId ,Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
//        model.addAttribute("post",postId);
        CommentDto comment = new CommentDto();
        comment.setPostId(postId);
        model.addAttribute("comment", comment);
        return "createCommentView";
    }

    @PostMapping("/{postId}/comment")
    public String createComment(@PathVariable int postId, @Valid @ModelAttribute("comment") CommentDto dto,
                                BindingResult errors,
                                Model model,
                                HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetUserFromSession(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/loginView";
        }

        if (errors.hasErrors()) {
            return "createCommentView";
        }
        try {
            Comment comment = commentMapper.fromDto(dto);
            Post post = postService.getPostById(postId);
            commentServices.createComment(comment, post, user);
            return "redirect:/posts/"+postId;
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            errors.rejectValue("name", "duplicate_comment_name");
            return "createCommentView";
        }
    }

}
