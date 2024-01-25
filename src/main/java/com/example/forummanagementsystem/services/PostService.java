package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.filters.FilterOptions;

import java.sql.Timestamp;
import java.util.List;

public interface PostService {
    List<Post> get(FilterOptions filterOptions);

    Post getPostById(int id);

    Post getPostByTitle(String title);

    List<Post> getPostByTimestamp(Timestamp timestampCreated);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(int id, User user);

    public void likePost(int id, User user);

    public void dislikePost(int id, User user);
}
