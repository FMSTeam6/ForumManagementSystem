package com.example.forummanagementsystem.services.impl;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizedOperationException;
import com.example.forummanagementsystem.mappers.CommentMapper;
import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dto.CommentDto;
import com.example.forummanagementsystem.repositories.CommentRepository;
import com.example.forummanagementsystem.repositories.PostRepository;
import com.example.forummanagementsystem.services.CommentServices;
import com.example.forummanagementsystem.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentServices {
    public static final String ERROR_MESSAGE = "You are not authorized!";
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
                              CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Comment> getAllCommentFromPost(int postId) {
        Post post = postRepository.getPostById(postId);
        try {
            return commentRepository.getAllCommentFromPost(postId);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Post", postId);
        }
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.getAll();
    }

    @Override
    public Comment getById(int id) {
        return commentRepository.getById(id);
    }

    @Override
    public void createComment(Comment comment, Post post, User user) {
        checkIfBanned(user);
        comment.setPost(post);
        comment.setAuthor(user);
        //TODO user must be add comment - List<Comment> commentList
        user.getComments().add(comment);
        post.getComments().add(comment);
        commentRepository.createComment(comment);
    }

    @Override
    public void updateComment(Comment comment, Post post, User user) {
        checkAuthor(comment, user);
        checkIfBanned(user);
        commentRepository.updateComment(comment);
//        user.getComments().stream()
//                .filter(comment1 -> comment1.getCommentId() = comment.getCommentId())
//                .findFirst()
//                .orElseThrow(new EntityNotFoundException("Comment", comment.getCommentId()));
    }

    @Override
    public void deleteComment(int id, User user, Post post) {
        checkIfUserAuthorOrAdmin(id, user);
        //TODO user must be delete comment from commentList
        commentRepository.deleteComment(id);
        user.getComments().remove(id);
        post.getComments().remove(id);

    }

    @Override
    public List<Comment> getAuthorComment(int authorId) {
        return commentRepository.getAuthorComment(authorId);
    }

    private void checkAuthor(Comment comment, User userToCheckAuthor) {
        if (comment.getAuthor().getId() != userToCheckAuthor.getId()) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }

    private void checkIfUserAuthorOrAdmin(int commentId, User user) {
        Comment comment = commentRepository.getById(commentId);
        if (!user.isAdmin() && comment.getAuthor().getId() != user.getId()) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }

    private void checkIfBanned(User user) {
        if (user.isBanned()) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }
}
