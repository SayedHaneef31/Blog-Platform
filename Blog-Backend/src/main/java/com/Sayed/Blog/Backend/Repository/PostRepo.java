package com.Sayed.Blog.Backend.Repository;

import com.Sayed.Blog.Backend.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PostRepo extends JpaRepository<Post, UUID> {
}
