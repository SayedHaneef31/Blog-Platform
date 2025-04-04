package com.Sayed.Blog.Backend.Service;

import com.Sayed.Blog.Backend.Entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> listCategories();

    Category saveCategory(Category category);

    void deletCategoryByID(UUID id);

    Category updateCategory(Category category);

    Category getCategoryById(UUID categoryId);
}
