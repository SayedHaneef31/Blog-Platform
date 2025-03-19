package com.Sayed.Blog.Backend.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter@Setter
@NoArgsConstructor@AllArgsConstructor@Builder
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)    //   JPA will automatically generate the uuid if it's null on its own
    @Column(updatable = false,nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;


    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL,orphanRemoval = true)                       //one to many as in one user can write many post
    private List<Post> posts=new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist    //used on a method that should be executed before the entity is persisted (saved) to the database.
    protected void onCreated()
    {
        createdAt=LocalDateTime.now();
    }


    public User(LocalDateTime createdAt, String email, UUID id, String name, String password, List<Post> posts) {
        this.createdAt = createdAt;
        this.email = email;
        this.id = id;
        this.name = name;
        this.password = password;
        this.posts = posts;
    }

    public User() {
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
