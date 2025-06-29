package com.Sayed.Blog.Backend.Controller;

import com.Sayed.Blog.Backend.Entity.*;
import com.Sayed.Blog.Backend.Entity.DTO.CreatePostRequestDto;
import com.Sayed.Blog.Backend.Entity.DTO.PostDto;
import com.Sayed.Blog.Backend.Repository.UserRepo;
import com.Sayed.Blog.Backend.Service.CategoryService;
import com.Sayed.Blog.Backend.Service.PostService;
import com.Sayed.Blog.Backend.Service.TagService;
import com.Sayed.Blog.Backend.Service.UserService;
import com.Sayed.Blog.Backend.Security.BlogUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private PostDto mapToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setStatus(post.getStatus().name());
        dto.setReadingTime(post.getReadingTime());
        dto.setAuthorEmail(post.getAuthor().getEmail());
        dto.setCategoryName(post.getCategory().getName());
        dto.setTagNames(
                post.getTags() != null
                        ? post.getTags().stream().map(Tag::getName).collect(Collectors.toSet())
                        : null
        );
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam(required = false) UUID categoryId,
                                                     @RequestParam(required = false) UUID tagId) {
        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream().map(this::mapToDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<Post>> getDrafts() {
        System.out.println("Inside getDrafts method in controller");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof BlogUserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        BlogUserDetails userDetails = (BlogUserDetails) authentication.getPrincipal();
        User loggedInUser = userDetails.getUser();
        
        System.out.println("User id=" + loggedInUser.getId());
        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
        return ResponseEntity.ok(draftPosts);
    }

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequestDto createPostRequestDto) {

        try {
            // Get authenticated user from SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof BlogUserDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
            }
            
            BlogUserDetails userDetails = (BlogUserDetails) authentication.getPrincipal();
            User loggedInUser = userDetails.getUser();
            
            System.out.println("Creating post for user: " + loggedInUser.getEmail());

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
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(savedPost));

        } catch (Exception e) {
            System.out.println("Error creating post: " + e.getMessage());
            e.printStackTrace();
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
