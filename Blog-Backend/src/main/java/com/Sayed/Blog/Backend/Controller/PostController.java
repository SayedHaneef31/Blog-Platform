package com.Sayed.Blog.Backend.Controller;

import com.Sayed.Blog.Backend.Entity.*;
import com.Sayed.Blog.Backend.Entity.DTO.CreatePostRequestDto;
import com.Sayed.Blog.Backend.Entity.DTO.PostDto;
import com.Sayed.Blog.Backend.Entity.DTO.AuthorDto;
import com.Sayed.Blog.Backend.Entity.DTO.CategoryDto;
import com.Sayed.Blog.Backend.Entity.DTO.TagDto;
import com.Sayed.Blog.Backend.Repository.UserRepo;
import com.Sayed.Blog.Backend.Security.BlogUserDetails;
import com.Sayed.Blog.Backend.Service.CategoryService;
import com.Sayed.Blog.Backend.Service.PostService;
import com.Sayed.Blog.Backend.Service.TagService;
import com.Sayed.Blog.Backend.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.AccessDeniedException;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam(required = false) UUID categoryId,
                                     @RequestParam(required = false) UUID tagId,
                                     @RequestParam(required = false, defaultValue = "false") boolean all)
    {
        //If all=true is passed, it fetches all posts, regardless of status.
        List<Post> posts;
        if (all) {
            posts = postService.listAllPosts();
        } else {
            posts = postService.getAllPosts(categoryId, tagId);
        }
        List<PostDto> postDtos = posts.stream().map(this::toPostDto).toList();
        return ResponseEntity.ok(postDtos);

    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable UUID postId) {
        try {
            Post post = postService.getPostById(postId);
            return ResponseEntity.ok(toPostDto(post));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

//    @GetMapping(path = "/drafts")
//    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID userId) {
//        System.out.println("Inside getDrafts method in controller");
//        System.out.println("User id="+userId);
//        User loggedInUser = userService.getUserById(userId);
//        List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
//        List<PostDto> draftDtos = draftPosts.stream().map(this::toPostDto).toList();
//        return ResponseEntity.ok(draftDtos);
//    }

    @PostMapping
    public ResponseEntity<?> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto) {

       return postService.createNewPost(createPostRequestDto);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable UUID postId,
            @Valid @RequestBody CreatePostRequestDto postDto) {

        try {
            // 🔐 Get authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }

            BlogUserDetails userDetails = (BlogUserDetails) authentication.getPrincipal();
            User loggedInUser = userDetails.getUser();

            // 🔧 Call service
            Post updatedPost = postService.updatePostById(postId, postDto, loggedInUser);

            return ResponseEntity.ok(updatedPost);

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update post: " + e.getMessage()));
        }
    }

    // --- Mapping Methods ---
    private PostDto toPostDto(Post post) {
        AuthorDto authorDto = null;
        if (post.getAuthor() != null) {
            authorDto = new AuthorDto(
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getAuthor().getEmail()
            );
        }
        CategoryDto categoryDto = null;
        if (post.getCategory() != null) {
            categoryDto = new CategoryDto(
                post.getCategory().getId(),
                post.getCategory().getName(),
                null // Avoid recursion
            );
        }
        List<TagDto> tagDtos = null;
        if (post.getTags() != null) {
            tagDtos = post.getTags().stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName()))
                .toList();
        }
        return new PostDto(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            authorDto,
            categoryDto,
            tagDtos,
            post.getReadingTime(),
            post.getStatus() != null ? post.getStatus().toString() : null,
            post.getCreatedAt(),
            post.getUpdatedAt()
        );
    }

    @GetMapping("/drafts")
    public ResponseEntity<?> getUserDrafts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedAt,desc") String[] sort) {

        try {
            // 🔐 Authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }

            BlogUserDetails userDetails = (BlogUserDetails) auth.getPrincipal();
            User loggedInUser = userDetails.getUser();

            // 🔃 Build Pageable from sort param
            Sort sortObj = Sort.by(Sort.Order.desc("updatedAt")); // default
            if (sort.length == 2) {
                sortObj = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
            }
            Pageable pageable = PageRequest.of(page, size, sortObj);

            Page<Post> draftPosts = postService.getDraftPosts(loggedInUser, pageable);
            return ResponseEntity.ok(draftPosts);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch drafts: " + e.getMessage()));
        }
    }


}
