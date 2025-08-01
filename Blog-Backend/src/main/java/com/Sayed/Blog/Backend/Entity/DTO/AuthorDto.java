package com.Sayed.Blog.Backend.Entity.DTO;

import java.util.UUID;

public class AuthorDto {
    private UUID id;
    private String username;
    private String email;

    public AuthorDto() {}

    public AuthorDto(UUID id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
