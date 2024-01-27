package com.example.forummanagementsystem.controllers;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.mappers.CommentMapper;
import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dto.CommentDto;
import com.example.forummanagementsystem.services.CommentServices;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comments")
public class CommentRestController {

    private final CommentServices commentServices;

    private final PostService postService;

    private final UserService userService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentRestController(CommentServices commentServices, PostService postService, UserService userService, CommentMapper commentMapper) {
        this.commentServices = commentServices;
        this.postService = postService;
        this.userService = userService;
        this.commentMapper = commentMapper;
    }

    @GetMapping
    public List<CommentDto> getAll() {
        return commentServices.getAll().stream().map(commentMapper).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CommentDto getById(@PathVariable int id) {
        Comment comment = commentServices.getById(id);
        return commentMapper.apply(comment);
    }

    @GetMapping("/post/{postId}")
    public List<CommentDto> getCommentByPost(@PathVariable int postId) {
        return commentServices.getAllCommentFromPost(postId)
                .stream().map(commentMapper).toList();
    }

    @GetMapping("/user/{id}")
    public List<CommentDto> getAuthor(@PathVariable int id) {
        try {
            return commentServices.getAuthorComment(id).stream().map(commentMapper).collect(Collectors.toList());
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }

//    @PostMapping("/create")
//    public void createComment(@RequestBody Comment comment, @RequestBody Post post, @RequestBody User user){
//        User user1 = userService.getById(user.getId());
//        Post post1 = postService.getPostById(post.getId());
//        commentServices.createComment(comment,post1,user1);
//    }

}
