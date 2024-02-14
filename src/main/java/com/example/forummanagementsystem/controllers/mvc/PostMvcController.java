package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.mappers.PostMapper;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dto.PostDto;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    public PostMvcController(PostService postService, UserService userService, PostMapper postMapper) {
        this.postService = postService;
        this.userService = userService;
        this.postMapper = postMapper;
    }

    @ModelAttribute("requestUPI")
    public String requestURI(final HttpServletRequest request){
        return request.getRequestURI();
    }

    @GetMapping("/new")
    public String createNewPost(Model model) {
        model.addAttribute("post", new PostDto());
        return "createPostView";
    }
    //TODO CREATE NEW POST

    @PostMapping("/new")
    public String createNewPost(@Valid @ModelAttribute("post") PostDto postDto, BindingResult errors, Model model) {

        if (errors.hasErrors()) {
            return "createPostView";
        }
        //TODO user MVS AUTHENTICATION
        try {
            User user = userService.getById(1);
            Post post = postMapper.fromDto(postDto);
            postService.create(post, user);
            return "redirect:/";
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
    public String updatePost(@PathVariable int id, Model model ){
        try {
            Post post = postService.getPostById(id);
            PostDto dto = postMapper.toDto(post);
            model.addAttribute("postId",id);
            model.addAttribute("post",dto);
            return "updateOrDeletePostView";
        }catch (EntityNotFoundException e){
            model.addAttribute("statusCode",HttpStatus.NOT_FOUND.is1xxInformational());
            model.addAttribute("error",e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/update")
    public String updateBeer(@PathVariable int id,
                             @Valid @ModelAttribute("post") PostDto dto,
                             BindingResult bindingResult,Model model) {

        if (bindingResult.hasErrors()) {
            return "updateOrDeletePostView";
        }

        try {
            User user = userService.getById(1);
            Post post = postMapper.fromDto(id, dto);
            postService.update(post, user);
            return "redirect:/";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("name", "duplicate_beer", e.getMessage());
            return "updateOrDeletePostView";
        }
    }
    //TODO DELETE

    @GetMapping("/{id}/delete")
    public String deleteBeer(@PathVariable int id, Model model) {

        try {
            User user = userService.getById(1);
            postService.delete(id, user);
            return "redirect:/beers";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
}
