package com.Sayed.Blog.Backend.Service;

import com.Sayed.Blog.Backend.Entity.DTO.CreatePostRequestDto;
import com.Sayed.Blog.Backend.Entity.Post;
import com.Sayed.Blog.Backend.Entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> listAllPosts();

    List<Post> getAllPosts(UUID categoryId, UUID tagId);

    Post getPostById(UUID postId);

    List<Post> getDraftPosts(User loggedInUser);

    ResponseEntity<?> createNewPost(CreatePostRequestDto createPostRequestDto);

    Post updatePostById(UUID postId, CreatePostRequestDto postDto, User currentUser);

}
