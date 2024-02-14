package com.example.forummanagementsystem.models.filters;

import java.util.Optional;

public class SearchUser {
    private Optional<String> username;
    private Optional<String> email;
    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public SearchUser(String username,
                      String email,
                      String firstName,
                      String lastName,
                      String sortBy,
                      String sortOrder) {
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public SearchUser() {
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
