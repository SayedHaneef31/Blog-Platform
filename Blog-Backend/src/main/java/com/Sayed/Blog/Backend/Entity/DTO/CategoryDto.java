package com.Sayed.Blog.Backend.Entity.DTO;

import java.util.List;
import java.util.UUID;

public class CategoryDto {
    private UUID id;
    private String name;
    private List<PostDto> posts;

    public CategoryDto() {}

    public CategoryDto(UUID id, String name, List<PostDto> posts) {
        this.id = id;
        this.name = name;
        this.posts = posts;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<PostDto> getPosts() { return posts; }
    public void setPosts(List<PostDto> posts) { this.posts = posts; }
} 