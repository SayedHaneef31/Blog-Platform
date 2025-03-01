package com.Sayed.Blog.Backend.Controller;

import com.Sayed.Blog.Backend.Entity.Category;
import com.Sayed.Blog.Backend.Repository.CategoryRepo;
import com.Sayed.Blog.Backend.Service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@RequiredArgsConstructor     // would automatically generate the constructor if any variable declared with final keyword....Alternative to @Autowired
//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping(path = "/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepo categoryRepo;


    @GetMapping
    public ResponseEntity<List<Category>> listAllCategories()
    {
        //List<Category> list=
        return ResponseEntity.ok(categoryService.listCategories());
    }


    //CREATE CATEGORY
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category)
    {
        return ResponseEntity.ok(categoryService.saveCategory(category));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id)
    {
        categoryService.deletCategoryByID(id);
        return ResponseEntity.ok().build();
//        The .build() method is used to create an instance of ResponseEntity with the configured status,
//            headers, and body. In this case, since we called ResponseEntity.ok().build(),
//            it creates a ResponseEntity with an HTTP status of 200 (OK) and no body content.
    }


//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateCategory(@PathVariable UUID id,@RequestBody Category category)
//    {
//        Optional<Category> optionalCategory = categoryRepo.findById(id);
//
//        if (optionalCategory.isPresent()) {
//            Category existingCategory = optionalCategory.get();
//            existingCategory.setName(updatedCategory.getName());  // Directly set the name
//            categoryRepository.save(existingCategory);  // Save to DB
//            return ResponseEntity.ok(existingCategory);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//
//    }
}
