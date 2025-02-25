package com.Sayed.Blog.Backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name="tag")
public class Tag
{
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;



}
