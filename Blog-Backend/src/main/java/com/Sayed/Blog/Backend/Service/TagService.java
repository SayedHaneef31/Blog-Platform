package com.Sayed.Blog.Backend.Service;

import com.Sayed.Blog.Backend.Entity.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;
import java.util.UUID;


public interface TagService
{
    List<Tag> listTags();

    Tag getTagById(UUID tagId);

    Set<Tag> getTagByIds(Set<UUID> ids);

    Tag createTag(Tag tag);

    void deleteTag(UUID tagId);
}
