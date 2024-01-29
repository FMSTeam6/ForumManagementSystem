package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dto.CommentDto;

import java.util.List;

public interface CommentServices {

    List<Comment> getAllCommentFromPost(int postId);

    List<Comment> getAll();

    Comment getById(int id);

    void createComment(Comment comment, Post post, User user);

    void updateComment(Comment comment, Post post, User user);

    void deleteComment(int id, User user, Post post);
    List<Comment> getAuthorComment(int authorId);
}
