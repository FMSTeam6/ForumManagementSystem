package com.example.forummanagementsystem.services.impl;

import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.Tag;
import com.example.forummanagementsystem.repositories.TagRepository;
import com.example.forummanagementsystem.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.getAll();
    }

    @Override
    public void create(Tag tag) {
        boolean duplicateTitleExists = true;
        try {
            tagRepository.getTagByName(tag.getContent());
        } catch (EntityNotFoundException e) {
            duplicateTitleExists = false;
        }

        if (duplicateTitleExists) {
            throw new EntityDuplicateException("Post", "title", tag.getContent());
        }

        tagRepository.create(tag);
    }

    @Override
    public Tag getTagByName(String content) {
        return tagRepository.getTagByName(content);
    }

    @Override
    public Tag getTagById(int id) {
        return tagRepository.getTagById(id);
    }
}
