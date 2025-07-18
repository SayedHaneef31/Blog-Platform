package com.Sayed.Blog.Backend.Entity.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class PostDto {
    private UUID id;
    private String title;
    private String content;
    private AuthorDto author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Add other fields as needed (e.g., status, readingTime)

    public PostDto() {}

    public PostDto(UUID id, String title, String content, AuthorDto author, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public AuthorDto getAuthor() { return author; }
    public void setAuthor(AuthorDto author) { this.author = author; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
