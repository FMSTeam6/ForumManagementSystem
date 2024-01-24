package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.models.filters.FilterOptions;
import com.example.forummanagementsystem.models.Post;

import java.sql.Timestamp;
import java.util.List;

public interface PostRepository {
    List<Post> get(FilterOptions filterOptions);

    Post getPostById(int id);

    Post getPostByName(String title);

    Post getPostByLikes(int likes);

    Post getPostByDislikes(int dislikes);

    Post getPostByTimeStamp(Timestamp timestampCreated);

    void create(Post post);

    Post updatePost(Post post);

    void deletePost(int id);

    int likePost(Post post);

    int dislikePost(Post post);
}
