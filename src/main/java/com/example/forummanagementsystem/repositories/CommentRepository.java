package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CommentRepository {

    List<Comment> getAllCommentFromPost(int postId);
    List<Comment> getAll();
    Comment getById(int id);
    void createComment(Comment comment);
    void updateComment(Comment comment);
    void deleteComment(int id);
    User getAuthorComment(int authorId);
}
