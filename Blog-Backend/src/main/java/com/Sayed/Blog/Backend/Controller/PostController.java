package com.Sayed.Blog.Backend.Controller;

import com.Sayed.Blog.Backend.Entity.*;
import com.Sayed.Blog.Backend.Entity.DTO.CreatePostRequestDto;
//import com.Sayed.Blog.Backend.Entity.DTO.PostDto;
//mport com.Sayed.Blog.Backend.Entity.DTO.PostDto;
import com.Sayed.Blog.Backend.Repository.UserRepo;
import com.Sayed.Blog.Backend.Security.BlogUserDetails;
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
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(@RequestParam(required = false) UUID categoryId,
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

        return ResponseEntity.ok(posts);

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
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto) {

       return postService.createNewPost(createPostRequestDto);
    }


}
