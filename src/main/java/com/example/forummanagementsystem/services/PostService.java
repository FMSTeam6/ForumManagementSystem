package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.filters.FilterOptions;

import java.util.List;

public interface PostService {
    List<Post> get(FilterOptions filterOptions);

    List<Post> getAll();

    List<Post> mostCommented();

    Post getPostById(int id);

    Post getPostByTitle(String title);

    List<Post> getPostByTimestamp();

    List<Post> getPostByAuthor(String username);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(int id, User user);

    void likePost(int id, User user);

    void dislikePost(int id, User user);
}
