package com.Sayed.Blog.Backend.Entity.DTO;

import jakarta.validation.constraints.NotNull;

public class LoginUserDto {


    @NotNull(message = "Email cannot be null")
    private String email;
    @NotNull(message = "Password cannot be null")
    private String password;

    public @NotNull(message = "Email cannot be null") String getEmail() {
        return email;
    }

    public void setEmail(@NotNull(message = "Email cannot be null") String email) {
        this.email = email;
    }

    public @NotNull(message = "Password cannot be null") String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = "Password cannot be null") String password) {
        this.password = password;
    }
}
