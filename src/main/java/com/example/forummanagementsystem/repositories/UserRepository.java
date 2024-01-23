package com.example.forummanagementsystem.repositories;


import com.example.forummanagementsystem.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByFirstName(String firstName);

    void create(User user);

    void update(User user);

    boolean banUser(User user);

    boolean unBanUser(User user);

    boolean giveAdminRights(User user);

    boolean deleteAdminRights(User user);



}
