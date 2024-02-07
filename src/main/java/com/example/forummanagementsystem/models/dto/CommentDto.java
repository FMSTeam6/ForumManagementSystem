package com.example.forummanagementsystem.models.dto;

import jakarta.validation.constraints.NotEmpty;

public class CommentDto {
    @NotEmpty(message = "The text can't be empty")
    private String text;

    private int authorId;
    private int postId;
    public CommentDto() {
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}

