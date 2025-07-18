package com.Sayed.Blog.Backend.Service.impl;

import com.Sayed.Blog.Backend.Entity.Category;
import com.Sayed.Blog.Backend.Entity.DTO.CategoryDto;
import com.Sayed.Blog.Backend.Entity.DTO.PostDto;
import com.Sayed.Blog.Backend.Entity.DTO.AuthorDto;
import com.Sayed.Blog.Backend.Entity.DTO.TagDto;
import com.Sayed.Blog.Backend.Entity.Post;
import com.Sayed.Blog.Backend.Entity.User;
import com.Sayed.Blog.Backend.Repository.CategoryRepo;
import com.Sayed.Blog.Backend.Service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;


    @Override
    public List<CategoryDto> listCategories() {
        List<Category> list = categoryRepo.findAll();
        return list.stream().map(this::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        if (categoryRepo.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists");
        }
        return categoryRepo.save(category);
    }

    @Override
    public void deletCategoryByID(UUID id) {

        if(id==null) throw new IllegalArgumentException("ID could not be null");
        categoryRepo.deleteById(id);

    }

    @Override
    public Category updateCategory(Category category)
    {
        return null;
    }

    @Override
    public Category getCategoryById(UUID categoryId) {
        return categoryRepo.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id" + categoryId));
    }

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
            category.getId(),
            category.getName(),
            category.getPosts().stream().map(this::toPostDto).collect(Collectors.toList())
        );
    }

    private PostDto toPostDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                toAuthorDto(post.getAuthor()),
                // Add category
                post.getCategory() != null
                        ? new CategoryDto(post.getCategory().getId(), post.getCategory().getName(), null)
                        : null,
                // Add tags
                post.getTags() != null
                        ? post.getTags().stream()
                        .map(tag -> new TagDto(tag.getId(), tag.getName()))
                        .collect(Collectors.toList())
                        : null,
                post.getReadingTime(),
                post.getStatus() != null ? post.getStatus().name() : null,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    private AuthorDto toAuthorDto(User user) {
        return new AuthorDto(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        );
    }

}
