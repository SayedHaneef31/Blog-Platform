package com.Sayed.Blog.Backend.Service;

import com.Sayed.Blog.Backend.Entity.Category;
import com.Sayed.Blog.Backend.Entity.DTO.CategoryDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategoryDto> listCategories();

    Category saveCategory(Category category);

    void deletCategoryByID(UUID id);

    Category updateCategory(Category category);

    Category getCategoryById(UUID categoryId);

    CategoryDto toCategoryDto(Category category);
}
