package com.example.forummanagementsystem.models.dto;

import jakarta.validation.constraints.NotEmpty;

public record CommentDto(
        @NotEmpty(message = "The text can't be empty")
        String text
) {
}
