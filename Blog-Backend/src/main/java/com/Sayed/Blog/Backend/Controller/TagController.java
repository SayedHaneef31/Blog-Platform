package com.Sayed.Blog.Backend.Controller;

import com.Sayed.Blog.Backend.Entity.DTO.CreateTagDto;
import com.Sayed.Blog.Backend.Entity.Tag;
import com.Sayed.Blog.Backend.Repository.TagRepo;
import com.Sayed.Blog.Backend.Service.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
@CrossOrigin(origins = "http://localhost:5173")
public class TagController
{

    @Autowired
    private TagRepo tagRepo;

    @Autowired
    private TagService tagService;

    @GetMapping
    public List<Tag> getAllTags()
    {
        return tagService.listTags();
    }

    @PostMapping
    public ResponseEntity<?> createTag(@Valid @RequestBody CreateTagDto createTagDto) {

        // TODO if tag already prent then it shoulld show  a popup
        try {
            Tag tag = new Tag();
            tag.setName(createTagDto.getName());
            Tag createdTag = tagService.createTag(tag);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id) {
        try {
            tagService.deleteTag(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
