package com.Sayed.Blog.Backend.Service.impl;

import com.Sayed.Blog.Backend.Entity.*;
import com.Sayed.Blog.Backend.Repository.PostRepo;
import com.Sayed.Blog.Backend.Service.CategoryService;
import com.Sayed.Blog.Backend.Service.PostService;
import com.Sayed.Blog.Backend.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
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
    public Post savePost(Post newPost) {
        return postRepo.save(newPost);
    }

    @Override
    public List<Post> getDraftPosts(User loggedInUser) {
        return postRepo.findAllByAuthorAndStatus(loggedInUser, PostStatus.DRAFT);
    }
}
