package com.example.forummanagementsystem.repositories;


import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.filters.SearchUser;

import java.util.List;

public interface UserRepository {

    List<User> getAll(SearchUser searchUser);

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByFirstName(String firstName);

    void create(User user);

    void update(User user);

    void banUser(User user);

    void unBanUser(User user);

    void giveAdminRights(User user);

    void deleteAdminRights(User user);

    void deleteUser(int id);

}
