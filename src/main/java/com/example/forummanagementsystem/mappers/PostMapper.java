package com.example.forummanagementsystem.mappers;

import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.dto.PostDto;
import com.example.forummanagementsystem.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    private final TagService tagService;

    @Autowired
    public PostMapper(TagService tagService) {
        this.tagService = tagService;
    }

    public Post fromDto(int id, PostDto dto) {
        Post post = fromDto(dto);
        post.setId(id);
        return post;
    }

    public Post fromDto(PostDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
       // post.setTags(dto.getTag());
        return post;
    }
}
