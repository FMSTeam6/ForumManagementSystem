package com.example.forummanagementsystem.mappers;

import com.example.forummanagementsystem.models.dto.CommentDto;
import com.example.forummanagementsystem.models.Comment;

import com.example.forummanagementsystem.services.CommentServices;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CommentMapper {

    private final CommentServices commentServices;
    private final UserService userService;
    private final PostService postService;
    @Autowired
    public CommentMapper(CommentServices commentServices, UserService userService, PostService postService) {
        this.commentServices = commentServices;
        this.userService = userService;
        this.postService = postService;
    }


    public Comment fromDto(int id, CommentDto dto){
        Comment comment = fromDto(dto);
        comment.setCommentId(id);
        return comment;
    }

    public Comment fromDto(CommentDto dto){
        Comment comment = new Comment();
        comment.setText(dto.getText());
//        comment.setAuthor(userService.getById(dto.getAuthorId()));
      //  comment.setPost(postService.getPostById(dto.getPostId()));
        return comment;
    }

    public CommentDto toDto(Comment comment){
        CommentDto dto = new CommentDto();
        dto.setText(comment.getText());
        return dto;
    }


}
