package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizedOperationException;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.filters.FilterOptions;
import com.example.forummanagementsystem.repositories.PostRepository;
import com.example.forummanagementsystem.services.impl.PostServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static com.example.forummanagementsystem.TestHelpers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PostServiceImplTests {
    @Mock
    PostRepository mockPostRepository;

    @InjectMocks
    PostServiceImpl postService;

    @Test
    void get_Should_CallPostRepository() {
        // Arrange
        FilterOptions mockFilterOptions = createMockFilterOptions();
        Mockito.when(mockPostRepository.get(mockFilterOptions))
                .thenReturn(null);

        // Act
        postService.get(mockFilterOptions);

        // Assert
        Mockito.verify(mockPostRepository, Mockito.times(1))
                .get(mockFilterOptions);
    }

    @Test
    public void get_Should_ReturnPost_When_PostIdExist() {
        // Arrange
        Post mockPost = createMockPost();

        Mockito.when(mockPostRepository.getPostById(Mockito.anyInt()))
                .thenReturn(mockPost);

        // Act
        Post result = postService.getPostById(mockPost.getId());

        // Assert
        Assertions.assertEquals(mockPost, result);
    }

    @Test
    public void create_Should_CallPostRepository_When_PostWithSameTitleDoesNotExist() {
        // Arrange
        Post mockPost = createMockPost();
        User mockUser = createMockUser();

        Mockito.when(mockPostRepository.getPostByTitle(mockPost.getTitle()))
                .thenThrow(EntityNotFoundException.class);

        // Act
        postService.create(mockPost, mockUser);

        // Assert
        Mockito.verify(mockPostRepository, Mockito.times(1))
                .create(mockPost);
    }

    @Test
    public void create_Should_ThrowException_When_PostWithSameTitleExists() {
        // Arrange
        Post mockPost = createMockPost();
        User mockUser = createMockUser();

        Mockito.when(mockPostRepository.getPostByTitle(mockPost.getTitle()))
                .thenReturn(mockPost);

        // Act, Assert
        Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> postService.create(mockPost, mockUser));
    }

    @Test
    void update_Should_CallRepository_When_UserIsAuthor() {
        // Arrange
        Post mockPost = createMockPost();
        User mockUserCreator = mockPost.getPostCreatedBy();

        Mockito.when(mockPostRepository.getPostById(Mockito.anyInt()))
                .thenReturn(mockPost);

        Mockito.when(mockPostRepository.getPostByTitle(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);

        // Act
        postService.update(mockPost, mockUserCreator);

        // Assert
        Mockito.verify(mockPostRepository, Mockito.times(1))
                .updatePost(mockPost);
    }

    @Test
    void update_Should_CallRepository_When_UserIsAdmin() {
        // Arrange
        User mockUserAdmin = createMockAdmin();
        Post mockPost = createMockPost();

        Mockito.when(mockPostRepository.getPostById(Mockito.anyInt()))
                .thenReturn(mockPost);

        Mockito.when(mockPostRepository.getPostByTitle(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);

        // Act
        postService.update(mockPost, mockUserAdmin);

        // Assert
        Mockito.verify(mockPostRepository, Mockito.times(1))
                .updatePost(mockPost);
    }

    @Test
    public void update_Should_CallRepository_When_UpdatingExistingPost() {
        // Arrange
        Post mockPost = createMockPost();
        User mockUserCreator = mockPost.getPostCreatedBy();

        Mockito.when(mockPostRepository.getPostById(Mockito.anyInt()))
                .thenReturn(mockPost);

        Mockito.when(mockPostRepository.getPostByTitle(Mockito.anyString()))
                .thenReturn(mockPost);

        // Act
        postService.update(mockPost, mockUserCreator);

        // Assert
        Mockito.verify(mockPostRepository, Mockito.times(1))
                .updatePost(mockPost);
    }

    @Test
    public void update_Should_ThrowException_When_UserIsNotCreator() {
        // Arrange
        Post mockPost = createMockPost();
        User mockUserCreator = mockPost.getPostCreatedBy();

        Mockito.when(mockPostRepository.getPostById(Mockito.anyInt()))
                .thenReturn(mockPost);

        // Act, Assert
        Assertions.assertThrows(
                UnauthorizedOperationException.class,
                () -> postService.update(mockPost, Mockito.mock(User.class)));

    }

    @Test
    public void update_Should_ThrowException_When_PostTitleIsTaken() {
        // Arrange
        Post mockPost = createMockPost();
        User mockUserCreator = mockPost.getPostCreatedBy();

        Mockito.when(mockPostRepository.getPostById(Mockito.anyInt()))
                .thenReturn(mockPost);

        Post mockExistingPostWithSameTitle = createMockPost();
        mockExistingPostWithSameTitle.setId(2);

        Mockito.when(mockPostRepository.getPostByTitle(mockPost.getTitle()))
                .thenReturn(mockExistingPostWithSameTitle);

        // Act, Assert
        Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> postService.update(mockPost, mockUserCreator));
    }

    @Test
    void delete_Should_CallRepository_When_UserIsAuthor() {
        // Arrange
        Post mockPost = createMockPost();
        User mockUserCreator = mockPost.getPostCreatedBy();

        Mockito.when(mockPostRepository.getPostById(Mockito.anyInt()))
                .thenReturn(mockPost);

        // Act
        postService.delete(1, mockUserCreator);

        // Assert
        Mockito.verify(mockPostRepository, Mockito.times(1))
                .deletePost(1);
    }

    @Test
    void delete_Should_CallRepository_When_UserIsAdmin() {
        // Arrange
        User mockUserAdmin = createMockAdmin();
        Post mockPost = createMockPost();

        Mockito.when(mockPostRepository.getPostById(Mockito.anyInt()))
                .thenReturn(mockPost);

        // Act
        postService.delete(1, mockUserAdmin);

        // Assert
        Mockito.verify(mockPostRepository, Mockito.times(1))
                .deletePost(1);
    }

    @Test
    void delete_Should_ThrowException_When_UserIsNotAdminOrAuthor() {
        // Arrange
        Post mockPost = createMockPost();

        Mockito.when(mockPostRepository.getPostById(Mockito.anyInt()))
                .thenReturn(mockPost);

        User mockUser = Mockito.mock(User.class);

        // Act, Assert
        Assertions.assertThrows(
                UnauthorizedOperationException.class,
                () -> postService.delete(1, mockUser));
    }
}
