package com.example.forummanagementsystem.models.dto.userDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterDto extends LoginDto{
    @NotEmpty(message = "Password confirmation can't be empty.")
    private String passwordConfirm;
    @NotEmpty(message = "First name confirmation can't be empty.")
    @Size(min=4,max = 32, message = "First name must be between 4 and 32 symbols")
    private String firstName;
    @NotEmpty(message = "Last name can't be empty.")
    @Size(min=4,max = 32, message = "Last name must be between 4 and 32 symbols")
    private String lastName;
    @Email(message = "Email must be valid.")
    @NotEmpty(message = "Email name can't be empty.")
    private String email;

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
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
