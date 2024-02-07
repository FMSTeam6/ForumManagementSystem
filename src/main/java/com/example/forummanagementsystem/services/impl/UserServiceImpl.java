package com.example.forummanagementsystem.services.impl;

import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UserStatusCannotBeChangedException;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.repositories.UserRepository;
import com.example.forummanagementsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public User getByFirstName(String firstName) {
        return userRepository.getByFirstName(firstName);
    }

    @Override
    public void create(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }
        userRepository.create(user);
    }

    @Override
    public void update(User user) {
        User userToUpdate = userRepository.getById(user.getId());

        try {
            User user1 = userRepository.getById(user.getId());
        } catch (EntityNotFoundException e) {

            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setPassword(user.getPassword());


            userRepository.update(userToUpdate);
        }

    }

    @Override
    public void banUser(User user) {
        User userToBan = userRepository.getById(user.getId());
        if (userToBan.isBanned()) {
            throw new UserStatusCannotBeChangedException(userToBan.getUsername(), "banned");
        }
        userRepository.banUser(userToBan);
    }

    @Override
    public void unBanUser(User user) {
        User userToUnBan = userRepository.getById(user.getId());
        if (!userToUnBan.isBanned()) {
            throw new UserStatusCannotBeChangedException(userToUnBan.getUsername(), "unbanned");
        }
        userRepository.unBanUser(userToUnBan);
    }

    @Override
    public void giveAdminRights(User user) {
        User userToReceiveAdminRights = userRepository.getById(user.getId());
        if (userToReceiveAdminRights.isAdmin()) {
            throw new UserStatusCannotBeChangedException(userToReceiveAdminRights.getUsername(), "admin");
        }
        userRepository.giveAdminRights(userToReceiveAdminRights);
    }

    @Override
    public void deleteAdminRights(User user) {
        User userToRemoveAdminRights = userRepository.getById(user.getId());
        if (!userToRemoveAdminRights.isAdmin()) {
            throw new UserStatusCannotBeChangedException(userToRemoveAdminRights.getUsername(), "not an admin");
        }
        userRepository.deleteAdminRights(userToRemoveAdminRights);
    }
}
