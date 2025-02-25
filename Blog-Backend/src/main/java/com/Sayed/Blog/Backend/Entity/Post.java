package com.Sayed.Blog.Backend.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter@Setter
@NoArgsConstructor@AllArgsConstructor
@Builder
@Table(name = "posts")
public class Post
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)    //   JPA will automatically generate the uuid if it's null on its own
    @Column(updatable = false,nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;


    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;


    @ManyToOne(fetch = FetchType.LAZY)      //many to one as in many post can be written by the single user
    @JoinColumn(name = "author_id", nullable = false)
    private User author;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
           name="post_tags",
           joinColumns = @JoinColumn(name="post_id"),
           inverseJoinColumns = @JoinColumn(name="tag_id")
    )
    private Set<Tag> tags=new HashSet<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @Column(nullable = false)
    private Integer readingTime;


    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreated()
    {
        LocalDateTime now=LocalDateTime.now();
        createdAt=now;
        updatedAt=now;
    }

    @PreUpdate
    protected void onUpdate()
    {
        updatedAt=LocalDateTime.now();
    }


}
