package com.Sayed.Blog.Backend.Service.impl;

import com.Sayed.Blog.Backend.Entity.Tag;
import com.Sayed.Blog.Backend.Entity.DTO.TagDto;
import com.Sayed.Blog.Backend.Repository.TagRepo;
import com.Sayed.Blog.Backend.Service.TagService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepo tagRepo;

    @Override
    public List<TagDto> listTags() {
        return tagRepo.findAll().stream().map(this::toTagDto).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Tag getTagById(UUID tagId) {
        return tagRepo.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with ID " + tagId));
    }

    @Override
    public Set<Tag> getTagByIds(Set<UUID> ids) {
        return tagRepo.findAllById(ids).stream().collect(Collectors.toSet());
    }

    @Override
    public Tag createTag(Tag tag) {
        if (tagRepo.existsByName(tag.getName())) {
            throw new IllegalArgumentException("Tag with name '" + tag.getName() + "' already exists");
        }
        return tagRepo.save(tag);
    }

    @Override
    public void deleteTag(UUID tagId) {
        if (!tagRepo.existsById(tagId)) {
            throw new EntityNotFoundException("Tag not found with ID " + tagId);
        }
        tagRepo.deleteById(tagId);
    }

    private TagDto toTagDto(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }
}
