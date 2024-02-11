package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizedOperationException;
import com.example.forummanagementsystem.exceptions.UserStatusCannotBeChangedException;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.repositories.UserRepository;
import com.example.forummanagementsystem.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.forummanagementsystem.TestHelpers.createMockPost;
import static com.example.forummanagementsystem.TestHelpers.createMockUser;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {
    @Mock
    UserRepository mockUserRepository;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void get_Should_ReturnUser_When_UserIdExist() {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.getById(Mockito.anyInt()))
                .thenReturn(mockUser);

        // Act
        User result = userService.getById(mockUser.getId());

        // Assert
        Assertions.assertEquals(mockUser, result);
    }

    @Test
    public void create_Should_CallPostRepository_When_UserWithSameEmailDoesNotExist() {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.getByEmail(mockUser.getEmail()))
                .thenThrow(EntityNotFoundException.class);

        // Act
        userService.create(mockUser);

        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .create(mockUser);
    }

    @Test
    public void create_Should_ThrowException_When_UserWithSameEmailExists() {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.getByEmail(mockUser.getEmail()))
                .thenReturn(mockUser);

        // Act, Assert
        Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> userService.create(mockUser));
    }

    @Test
    void testUpdate_UserNotFound() {
        // Arrange
        User mockUser = createMockUser();
        Mockito.when(mockUserRepository.getById(mockUser.getId()))
                .thenThrow(EntityNotFoundException.class);

        // Act and Assert
        Assertions.assertThrows(
                EntityNotFoundException.class, () -> userService.update(mockUser));
    }

    @Test
    void testBanUser_UserNotBanned() {
        // Arrange
        User mockUser = createMockUser();
        Mockito.when(mockUserRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        // Act
        userService.banUser(mockUser);

        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .banUser(mockUser);
    }

    @Test
    void testBanUser_UserAlreadyBanned() {
        // Arrange
        User mockUser = createMockUser();
        mockUser.setBanned(true);
        Mockito.when(mockUserRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        // Act and Assert
        Assertions.assertThrows(
                UserStatusCannotBeChangedException.class, () -> userService.banUser(mockUser));
    }

    @Test
    void testGiveAdminRights_UserNotAdmin() {
        // Arrange
        User mockUser = createMockUser();
        Mockito.when(mockUserRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        // Act
        userService.giveAdminRights(mockUser);

        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .giveAdminRights(mockUser);
    }

    @Test
    void testMakeUserAdmin_UserAlreadyAdmin() {
        // Arrange
        User mockUser = createMockUser();
        mockUser.setAdmin(true);
        Mockito.when(mockUserRepository.getById(mockUser.getId()))
                .thenReturn(mockUser);

        // Act and Assert
        Assertions.assertThrows(
                UserStatusCannotBeChangedException.class, () -> userService.giveAdminRights(mockUser));
    }
}
