package com.Sayed.Blog.Backend.Controller;

import com.Sayed.Blog.Backend.Entity.Tag;
import com.Sayed.Blog.Backend.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController
{
    @Autowired
    private TagService tagService;

    @GetMapping
    public List<Tag> getAllTags()
    {
        return tagService.listTags();
    }
}
