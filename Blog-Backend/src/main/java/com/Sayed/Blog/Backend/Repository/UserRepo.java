package com.Sayed.Blog.Backend.Repository;

import com.Sayed.Blog.Backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID>
//Since we are implementing with the help of jpa......It asks for 1.Type of Entity we are dealing with 2. And the id
//It's empty but String Data Jpa will provide us all the implementation
{
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
