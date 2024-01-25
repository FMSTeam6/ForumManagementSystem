package com.example.forummanagementsystem.repositories.impl;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.filters.FilterOptions;
import com.example.forummanagementsystem.repositories.PostRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // TODO - When we implement filter options class
    @Override
    public List<Post> get(FilterOptions filterOptions) {
        return null;
    }

    @Override
    public Post getPostById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return post;
        }
    }

    @Override
    public Post getPostByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                    "from Post where title = :title", Post.class);
            query.setParameter("title", title);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Post", "title", title);
            }
            return result.get(0);
        }
    }

    // TODO - We should try this method for the 10 recently created posts when service is done
    @Override
    public List<Post> getPostByTimeStamp(Timestamp timestampCreated) {
        try (Session session = sessionFactory.openSession()) {
            Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
            Query<Post> query = session.createQuery(
                    "from Post where timestampCreated <= :currentTime limit 10", Post.class);
            query.setParameter("timestamp_created", currentTime);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException(
                        "Post", "timestampCreated", timestampCreated.toString());
            }
            return result.stream()
                    .filter(p -> p.getTimestampCreated().compareTo(currentTime) < 0)
                    .limit(10)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void create(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public Post updatePost(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
            return post;
        }
    }

    @Override
    public void deletePost(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getPostById(id));
            session.getTransaction().commit();
        }
    }

    @Override
    public int likePost(int id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return post.getLikes() + 1;
        }
    }

    @Override
    public int dislikePost(int id) {
        try (Session session = sessionFactory.openSession()) {
            Post post = session.get(Post.class, id);
            if (post == null) {
                throw new EntityNotFoundException("Post", id);
            }
            return post.getDislikes() + 1;
        }
    }
}
