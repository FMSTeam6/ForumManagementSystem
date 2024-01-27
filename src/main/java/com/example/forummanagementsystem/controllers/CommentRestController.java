package com.example.forummanagementsystem.controllers;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.mappers.CommentMapper;
import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.dto.CommentDto;
import com.example.forummanagementsystem.services.CommentServices;
import com.example.forummanagementsystem.services.PostService;
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
    private final CommentMapper commentMapper;

    @Autowired
    public CommentRestController(CommentServices commentServices, PostService postService, CommentMapper commentMapper) {
        this.commentServices = commentServices;
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

    @PostMapping("/{postid}")
    public Comment createComment(@PathVariable int postId){
        return null;
    }

}
