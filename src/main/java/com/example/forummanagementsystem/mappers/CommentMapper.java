package com.example.forummanagementsystem.mappers;

import com.example.forummanagementsystem.models.dto.CommentDto;
import com.example.forummanagementsystem.models.Comment;

import com.example.forummanagementsystem.services.CommentServices;
import com.example.forummanagementsystem.services.PostService;
import org.springframework.stereotype.Component;

import java.util.function.Function;


@Component
public class CommentMapper implements Function<Comment,CommentDto> {

    private final PostService postService;

    public CommentMapper(PostService postService) {
        this.postService = postService;
    }

    @Override
    public CommentDto apply(Comment comment) {
        return new CommentDto(
                comment.getCommentId(),
                comment.getText(),
                comment.getAuthor().getUsername()
        );
    }
}
