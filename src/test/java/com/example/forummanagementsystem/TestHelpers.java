package com.example.forummanagementsystem;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.dto.PostDto;
import com.example.forummanagementsystem.models.filters.FilterOptions;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TestHelpers {
    public static User createMockAdmin() {
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        return mockUser;
    }

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("TestSubject");
        mockUser.setPassword("TestPassword");
        mockUser.setFirstName("TestFirstName");
        mockUser.setLastName("TestLastName");
        mockUser.setEmail("test@test.com");
        return mockUser;
    }

    public static Post createMockPost() {
        Post mockPost = new Post();
        mockPost.setId(1);
        mockPost.setTitle("TestPurpose");
        mockPost.setPostCreatedBy(createMockUser());
        mockPost.setContent("This is a content with a testing purpose");
        return mockPost;
    }

    public static Comment createMockComment() {
        Comment mockComment = new Comment();
        mockComment.setCommentId(1);
        mockComment.setAuthor(createMockUser());
        mockComment.setText("This is a text with a testing purpose");
        return mockComment;
    }

    public static FilterOptions createMockFilterOptions() {
        LocalDateTime now = LocalDateTime.now();
        return new FilterOptions(
                "title",
                "username",
                Timestamp.valueOf(now),
                0,
                0,
                "sort",
                "order"
        );
    }

    public static PostDto createPostDto() {
        PostDto dto = new PostDto();
        dto.setTitle("TestPurpose");
        dto.setContent("This is a content with a testing purpose and nothing more");
        dto.setTag("TestTag");
        return dto;
    }

    public static String toJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
