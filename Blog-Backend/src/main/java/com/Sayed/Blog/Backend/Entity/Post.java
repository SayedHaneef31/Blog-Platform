package com.Sayed.Blog.Backend.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
@Builder
@Table(name = "post")
public class Post
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)    //   JPA will automatically generate the uuid if it's null on its own
    @Column(updatable = false,nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;


    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Category category;

    private Set<Tag> tags=new HashSet<>();

    private PostStatus postStatus;

    private Integer readingTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
