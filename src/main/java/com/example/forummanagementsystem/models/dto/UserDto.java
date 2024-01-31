package com.example.forummanagementsystem.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDto {
    @NotNull(message = "Choose your username")
    @Size(min = 3, max = 23, message = "Username should be between 3 and 23 symbols")
    private String username;
    @NotNull(message = "Choose your password")
    @Size(min = 2, max = 10, message = "Password should be between 2 and 10 symbols")
    private String password;
    @NotNull(message = "Please enter your first name")
    @Size(min = 3, max = 23, message = "First name should be between 3 and 23 symbols")
    private String firstName;
    @NotNull(message = "Please enter your last name")
    @Size(min = 3, max = 23, message = "Last name should be between 3 and 23 symbols")
    private String lastName;
    @NotNull(message = "Please enter your email")
    @Size(min = 5, max = 30, message = "Email should be between 5 and 30 symbols")
    private String email;

    public UserDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
