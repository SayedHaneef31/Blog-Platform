package com.Sayed.Blog.Backend.Controller;

import com.Sayed.Blog.Backend.Entity.*;
import com.Sayed.Blog.Backend.Entity.DTO.CreatePostRequestDto;
//import com.Sayed.Blog.Backend.Entity.DTO.PostDto;
//mport com.Sayed.Blog.Backend.Entity.DTO.PostDto;
import com.Sayed.Blog.Backend.Repository.UserRepo;
import com.Sayed.Blog.Backend.Service.CategoryService;
import com.Sayed.Blog.Backend.Service.PostService;
import com.Sayed.Blog.Backend.Service.TagService;
import com.Sayed.Blog.Backend.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final UserService userService;
    private final UserRepo userRepo;

    public PostController(PostService postService, CategoryService categoryService, TagService tagService, UserService userService, UserRepo userRepo) {
        //System.out.println("Inside post controller");
        this.postService = postService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(@RequestParam(required = false) UUID categoryId,
                                     @RequestParam(required = false) UUID tagId)
    {
        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        //List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(posts);
        //return postService.listAllPosts();
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<Post>> getDrafts(@RequestAttribute UUID userId) {
        System.out.println("Inside getDrafts method in controller");
        System.out.println("User id="+userId);
        User loggedInUser = userService.getUserById(userId);
        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
        return ResponseEntity.ok(draftPosts);
    }

    @PostMapping
    public ResponseEntity<?> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute(required = false) UUID userId) {

        try {
            // Validate user
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
            }

            User loggedInUser = userService.getUserById(userId);
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

            Post savedPost = postService.savePost(post);
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
