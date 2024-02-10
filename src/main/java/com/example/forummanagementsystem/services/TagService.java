package com.example.forummanagementsystem.services;

import com.example.forummanagementsystem.models.Tag;

import java.util.List;

public interface TagService {

    List<Tag> getAll();
    void create(Tag tag);

    Tag getTagByName(String content);

    Tag getTagById(int id);

}
