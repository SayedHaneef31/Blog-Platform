package com.Sayed.Blog.Backend.Repository;

import com.Sayed.Blog.Backend.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepo extends JpaRepository<Category, UUID> {
    boolean existsByName(String name);
}
