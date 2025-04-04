package com.Sayed.Blog.Backend.Entity.DTO;


import com.Sayed.Blog.Backend.Entity.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Builder
public class CreatePostRequestDto {

    @NotBlank(message = "title is required")
    @Size(min = 3,max = 25)
    private String title;

    @NotBlank(message = "content is required")
    @Size(min = 3,max = 100000)
    private String content;

    @NotNull(message = "category id is required")
    private UUID categoryId;

    @Builder.Default
    @Size(max = 5)
    private Set<UUID> tagIds =new HashSet<>();

    @NotNull(message = "Status is required")
    private PostStatus postStatus;

    public @NotNull(message = "category id is required") UUID getCategoryId() {
        return categoryId;
    }

    public @NotBlank(message = "content is required") @Size(min = 3, max = 100000) String getContent() {
        return content;
    }

    public @NotNull(message = "Status is required") PostStatus getPostStatus() {
        return postStatus;
    }

    public @Size(max = 5) Set<UUID> getTagIds() {
        return tagIds;
    }

    public @NotBlank(message = "title is required") @Size(min = 3, max = 25) String getTitle() {
        return title;
    }
}
