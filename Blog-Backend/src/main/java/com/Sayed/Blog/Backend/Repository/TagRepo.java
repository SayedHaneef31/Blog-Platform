package com.Sayed.Blog.Backend.Repository;

import com.Sayed.Blog.Backend.Entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TagRepo extends JpaRepository<Tag, UUID> {

    boolean existsByName(String namee);
}
