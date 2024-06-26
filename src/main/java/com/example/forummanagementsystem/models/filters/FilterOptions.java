package com.example.forummanagementsystem.models.filters;

import java.sql.Timestamp;
import java.util.Optional;

public class FilterOptions {
    private Optional<String> title;
    private Optional<String> author;
    private Optional<Timestamp> timestampCreated;
    private Optional<Integer> likes;
    private Optional<Integer> dislikes;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptions() {
        this.title = Optional.empty();
        this.author = Optional.empty();
        this.timestampCreated = Optional.empty();
        this.likes = Optional.empty();
        this.dislikes = Optional.empty();
        this.sortBy = Optional.empty();
        this.sortOrder = Optional.empty();
    }

    public FilterOptions(String title,
                         String author,
                         Timestamp timestampCreated,
                         Integer likes,
                         Integer dislikes,
                         String sortBy,
                         String sortOrder) {
        this.title = Optional.ofNullable(title);
        this.author = Optional.ofNullable(author);
        this.timestampCreated = Optional.ofNullable(timestampCreated);
        this.likes = Optional.ofNullable(dislikes);
        this.dislikes = Optional.ofNullable(likes);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getAuthor() {
        return author;
    }

    public Optional<Timestamp> getTimestampCreated() {
        return timestampCreated;
    }

    public Optional<Integer> getLikes() {
        return likes;
    }

    public Optional<Integer> getDislikes() {
        return dislikes;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
