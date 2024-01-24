package com.example.forummanagementsystem.repositories;

import com.example.forummanagementsystem.models.Tag;

import java.util.List;

public interface TagRepository {

    List<Tag> getAll();
    void create(Tag tag);

    Tag getTagByName(String content);

    Tag getTagById(int id);

}
