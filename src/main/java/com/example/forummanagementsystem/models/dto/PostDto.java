package com.example.forummanagementsystem.models.dto;

import com.example.forummanagementsystem.models.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class PostDto {
    @NotNull(message = "Please choose a meaningfully title")
    @Size(min = 4, max = 64, message = "Title should be between 16 and 64 symbols")
    private String title;
    @NotNull(message = "Content can't be empty")
    @Size(min = 32, max = 8192, message = "Content can be between 32 and 8192 symbols")
    private String content;
    @Size(min = 3, max = 12, message = "Tag can be between 3 and 12 symbols")
    private String tag;

    public PostDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
