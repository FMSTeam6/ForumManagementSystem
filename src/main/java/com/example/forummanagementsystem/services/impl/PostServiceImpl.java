package com.example.forummanagementsystem.services.impl;

import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizedOperationException;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.filters.FilterOptions;
import com.example.forummanagementsystem.repositories.PostRepository;
import com.example.forummanagementsystem.repositories.UserRepository;
import com.example.forummanagementsystem.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    public static final String ONLY_AUTHOR_CAN_MODIFY_CONTENT =
            "Only post author can modify the content of the post!";
    public static final String ONLY_ADMIN_OR_AUTHOR_CAN_REMOVE_POST =
            "Only admin or author can remove a post!";
    private final PostRepository postRepository;

    private final UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Post> get(FilterOptions filterOptions) {
        return postRepository.get(filterOptions);
    }

    @Override
    public Post getPostById(int id) {
        return postRepository.getPostById(id);
    }

    @Override
    public Post getPostByTitle(String title) {
        return postRepository.getPostByTitle(title);
    }

    @Override
    public List<Post> getPostByAuthor(String username) {
        return postRepository.getPostByAuthor(username);
    }

    @Override
    public List<Post> getPostByTimestamp(Timestamp timestampCreated) {

        return postRepository.getPostByTimeStamp(timestampCreated);
    }

    @Override
    public void create(Post post, User user) {
        boolean duplicateTitleExists = true;
        try {
            postRepository.getPostByTitle(post.getTitle());
        } catch (EntityNotFoundException e) {
            duplicateTitleExists = false;
        }

        if (duplicateTitleExists) {
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        }
      //  checkIfUserIsBannedOrDeleted(user);
        post.setPostCreatedBy(user);
        postRepository.create(post);
    }


    @Override
    public void update(Post post, User user) {
        boolean duplicateExists = true;
        try {
            Post existingPost = postRepository.getPostByTitle(post.getTitle());
            if (existingPost.getId() == post.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        }
       // checkIfUserIsBannedOrDeleted(user);
        checkUpdatePermissions(postRepository.getPostById(post.getId()), user);
        postRepository.updatePost(post);
    }

    @Override
    public void delete(int id, User user) {
       // checkIfUserIsBannedOrDeleted(user);
        checkDeletePermissions(postRepository.getPostById(id), user);
        postRepository.deletePost(id);
    }

    // TODO - How to show which users liked the post? Should there be another method in User?
    @Override
    public void likePost(int id, User user) {
        postRepository.likePost(id);
    }

    // TODO - How to show which users disliked the post? Should there be another method in User?
    @Override
    public void dislikePost(int id, User user) {
        postRepository.dislikePost(id);
    }

    private void checkUpdatePermissions(Post post, User user) {
        if (!post.getPostCreatedBy().equals(user)) {
            throw new UnauthorizedOperationException(ONLY_AUTHOR_CAN_MODIFY_CONTENT);
        }
    }

    private void checkDeletePermissions(Post post, User user) {
        if (!user.isAdmin()) {
            if (!post.getPostCreatedBy().equals(user)) {
                throw new UnauthorizedOperationException(ONLY_ADMIN_OR_AUTHOR_CAN_REMOVE_POST);
            }
        }
    }
//    private void checkIfUserIsBannedOrDeleted(User user){
//        if (user.isDeleted() || user.isBanned()){
//            throw new UnauthorizedOperationException("First log in to create posts");
//        }
//    }

//    private void checkIfUserIsLoggedIn(User user){
//        if(user.)
//    }
}

