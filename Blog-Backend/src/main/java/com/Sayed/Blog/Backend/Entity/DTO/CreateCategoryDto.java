package com.Sayed.Blog.Backend.Entity.DTO;

public class CreateCategoryDto {
    private String name;

    public CreateCategoryDto() {}

    public CreateCategoryDto(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
