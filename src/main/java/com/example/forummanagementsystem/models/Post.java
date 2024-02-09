package com.example.forummanagementsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table(name = "posts")
public class Post {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "likes")
    private int likes;

    @Column(name = "dislikes")
    private int dislikes;

    @Column(name = "timestamp_created")
    private final LocalDateTime getTime = LocalDateTime.now();


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User postCreatedBy;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "posts_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")

    )

    private Set<Tag> tags;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private List<Comment> comments;

    public Post() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public LocalDateTime getGetTime() {
        return getTime;
    }

    public User getPostCreatedBy() {
        return postCreatedBy;
    }

    public void setPostCreatedBy(User postCreatedBy) {
        this.postCreatedBy = postCreatedBy;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Post post = (Post) o;
//        return id == post.id && likes == post.likes && dislikes == post.dislikes && Objects.equals(title, post.title) && Objects.equals(getTime, post.getTime) && Objects.equals(postCreatedBy, post.postCreatedBy) && Objects.equals(tags, post.tags);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && likes == post.likes && dislikes == post.dislikes && Objects.equals(title, post.title) && Objects.equals(content, post.content) && Objects.equals(getTime, post.getTime) && Objects.equals(postCreatedBy, post.postCreatedBy) && Objects.equals(tags, post.tags) && Objects.equals(comments, post.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}