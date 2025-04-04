package com.Sayed.Blog.Backend.Service.impl;

import com.Sayed.Blog.Backend.Entity.Category;
import com.Sayed.Blog.Backend.Repository.CategoryRepo;
import com.Sayed.Blog.Backend.Service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;


    @Override
    public List<Category> listCategories() {
        List<Category> list= categoryRepo.findAll();
        return list;
    }

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        //LocalDateTime now = LocalDateTime.now();
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


}
