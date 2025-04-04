package com.Sayed.Blog.Backend.Entity.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class CreateTagDto {
    @NotBlank(message = "Tag name is required")
    @Size(min = 2, max = 50, message = "Tag name must be between 2 and 50 characters")
    private String name;

    public @NotBlank(message = "Tag name is required") @Size(min = 2, max = 50, message = "Tag name must be between 2 and 50 characters") String getName() {
        return name;
    }
}