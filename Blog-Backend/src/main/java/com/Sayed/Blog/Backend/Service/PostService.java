package com.Sayed.Blog.Backend.Service;

import com.Sayed.Blog.Backend.Entity.Post;
import com.Sayed.Blog.Backend.Entity.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    public List<Post> listAllPosts();

    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    Post savePost(Post newPost);

    List<Post> getDraftPosts(User loggedInUser);
}
