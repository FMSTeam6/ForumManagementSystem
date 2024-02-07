package com.example.forummanagementsystem.models.dto;

import jakarta.validation.constraints.NotEmpty;

public class CommentDto {
    @NotEmpty(message = "The text can't be empty")
    private String text;

    private int author;
    private int post;
    public CommentDto() {
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAuthorId() {
        return author;
    }

    public void setAuthorId(int authorId) {
        this.author = authorId;
    }

    public int getPostId() {
        return post;
    }

    public void setPostId(int postId) {
        this.post = postId;
    }
}

