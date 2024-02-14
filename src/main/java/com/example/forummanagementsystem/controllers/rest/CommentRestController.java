package com.example.forummanagementsystem.controllers.rest;

import com.example.forummanagementsystem.controllers.rest.AuthenticationHelper;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizedOperationException;
import com.example.forummanagementsystem.mappers.CommentMapper;
import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dto.CommentDto;
import com.example.forummanagementsystem.services.CommentServices;
import com.example.forummanagementsystem.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentRestController {

    private final CommentServices commentServices;

    private final PostService postService;

    private final CommentMapper commentMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CommentRestController(CommentServices commentServices,
                                 PostService postService,
                                 CommentMapper commentMapper,
                                 AuthenticationHelper authenticationHelper) {
        this.commentServices = commentServices;
        this.postService = postService;
        this.commentMapper = commentMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/comments/{postId}")
    public List<Comment> getAllFromPost(@PathVariable int postId) {
        return commentServices.getAllCommentFromPost(postId);
    }

    @GetMapping("/comment/{id}")
    public Comment getById(@PathVariable int id) {
        try {
            return commentServices.getById(id);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }

    @PostMapping("/create/{postId}")
    public void createComment(@RequestHeader HttpHeaders headers, @PathVariable int postId,
                              @Valid @RequestBody CommentDto dto){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(postId);
            Comment comment = commentMapper.fromDto(dto);
            commentServices.createComment(comment,post,user);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }catch (UnauthorizedOperationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }

    @PutMapping("/update/{postId}/comments/{id}")
    public void update(@RequestHeader HttpHeaders headers,@PathVariable int postId,@PathVariable int id,
                       @Valid @RequestBody CommentDto dto){
        try{
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(postId);
            Comment comment = commentMapper.fromDto(id,dto);
            commentServices.updateComment(comment,post,user);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }catch (UnauthorizedOperationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }

    @DeleteMapping("/delete/{postId}/comments/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int postId,
                       @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Post post = postService.getPostById(postId);
            commentServices.deleteComment(id,user,post);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }catch (UnauthorizedOperationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }

}
