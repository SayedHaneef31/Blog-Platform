package com.Sayed.Blog.Backend.Service.impl;

import com.Sayed.Blog.Backend.Entity.Tag;
import com.Sayed.Blog.Backend.Repository.TagRepo;
import com.Sayed.Blog.Backend.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepo tagRepo;

    @Override
    public List<Tag> listTags() {
        return tagRepo.findAll();
    }
}
