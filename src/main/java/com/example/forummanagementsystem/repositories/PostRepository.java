package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.filters.FilterOptions;
import com.example.forummanagementsystem.models.Post;

import java.sql.Timestamp;
import java.util.List;

public interface PostRepository {
    List<Post> get(FilterOptions filterOptions);

    Post getPostById(int id);

    Post getPostByTitle(String title);

    List<Post> getPostByTimeStamp();
    List<Post> getAll();

    List<Post> getPostByAuthor(String username);

    void create(Post post);

    Post updatePost(Post post);

    void deletePost(int id);

    void likePost(int id);

    void dislikePost(int id);
}
