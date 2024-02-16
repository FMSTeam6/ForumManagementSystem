package com.example.forummanagementsystem.models.dto.userDtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserUpdateDto {
    @NotNull(message = "Name can't be empty")
    @Size(min = 4, max = 32, message = "First name must be between 4 and 32 symbols.")
    private String firstName;
    @NotNull(message = "Name can't be empty")
    @Size(min = 4, max = 32, message = "Last name must be between 4 and 32 symbols.")
    private String lastName;

    @NotNull(message = "Password can't be empty")
    private String password;

    public UserUpdateDto() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
