package com.Sayed.Blog.Backend.Service.impl;

import com.Sayed.Blog.Backend.Entity.*;
import com.Sayed.Blog.Backend.Entity.DTO.CreatePostRequestDto;
import com.Sayed.Blog.Backend.Repository.PostRepo;
import com.Sayed.Blog.Backend.Security.BlogUserDetails;
import com.Sayed.Blog.Backend.Service.CategoryService;
import com.Sayed.Blog.Backend.Service.PostService;
import com.Sayed.Blog.Backend.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PostServiceImpl implements PostService {

//    public PostServiceImpl() {
//        System.out.println("PostServiceImpl Bean Initialized!");  // Check if bean is created
//    }

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;



    @Override
    public List<Post> listAllPosts() {

        System.out.println("in PostServiceImpl");
        if(postRepo.findAll().isEmpty()) {
            System.out.println("no post present");
            return new ArrayList<>();
        }
        else
            return postRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId)
    {
        if(categoryId != null && tagId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepo.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED,
                    category,
                    tag
            );
        }
        if(categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            return postRepo.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED,
                    category
            );
        }

        if(tagId != null) {
            Tag tag = tagService.getTagById(tagId);
            return postRepo.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED,
                    tag
            );
        }
        return postRepo.findAllByStatus(PostStatus.PUBLISHED);

    }



    @Override
    public List<Post> getDraftPosts(User loggedInUser) {
        return postRepo.findAllByAuthorAndStatus(loggedInUser, PostStatus.DRAFT);
    }

    @Override
    public ResponseEntity<?> createNewPost(CreatePostRequestDto createPostRequestDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }

            BlogUserDetails userDetails = (BlogUserDetails) authentication.getPrincipal();
            User loggedInUser = userDetails.getUser();
            if (loggedInUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }

            // Validate category
            Category category = categoryService.getCategoryById(createPostRequestDto.getCategoryId());
            if (category == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid category ID"));
            }

            // Create post
            Post post = new Post();
            post.setTitle(createPostRequestDto.getTitle());
            post.setContent(createPostRequestDto.getContent());
            post.setStatus(createPostRequestDto.getPostStatus());
            post.setReadingTime(calculateReadingTime(createPostRequestDto.getContent()));
            post.setAuthor(loggedInUser);
            post.setCategory(category);

            // Set tags if provided
            if (createPostRequestDto.getTagIds() != null && !createPostRequestDto.getTagIds().isEmpty()) {
                Set<Tag> tags = tagService.getTagByIds(createPostRequestDto.getTagIds());
                post.setTags(tags);
            }

            Post savedPost = postRepo.save(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create post: " + e.getMessage()));
        }
    }
    private Integer calculateReadingTime(String content) {
        String words[] = content.split("\\s+");
        int wordCount = words.length;
        return (int) Math.ceil((double) wordCount / 200);
    }
}
