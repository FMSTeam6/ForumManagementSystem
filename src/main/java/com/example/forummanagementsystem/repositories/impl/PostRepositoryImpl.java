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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Post> get(FilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" from Post ");
            ArrayList<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();
            filterOptions.getTitle().ifPresent(value -> {
                filters.add(" title like: postTitle ");
                params.put("postTitle", String.format("%%%s%%", value));
            });
            filterOptions.getLikes().ifPresent(value -> {
                filters.add(" likes >=: minLikes ");
                params.put("minLikes", value);
            });
            filterOptions.getDislikes().ifPresent(value -> {
                filters.add(" dislikes <=: maxDislikes ");
                params.put("maxDislikes", value);
            });
            filterOptions.getAuthor().ifPresent(value -> {
                filters.add(" postCreatedBy =: postCreatedBy ");
                params.put("postCreatedBy", value);
            });
            filterOptions.getTimestampCreated().ifPresent(value -> {
                filters.add(" timestamp_created >=: timestampCreated ");
                params.put("timestampCreated", value);
            });
            if (!filters.isEmpty()) {
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(filterOptions));
            Query<Post> query = session.createQuery(queryString.toString(), Post.class);
            query.setProperties(params);
            return query.list();
        }
    }
    private String generateOrderBy(FilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }
        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {
            case "title":
                orderBy = "title";
                break;
            case "author":
                orderBy = "user.username";
                break;
            case "timestampCreated":
                orderBy = "timestamp_created";
                break;
            case "likes":
                orderBy = "likes";
                break;
            case "dislikes":
                orderBy = "dislikes";
                break;
        }
        orderBy = String.format(" orderBy %s ", orderBy);
        if (filterOptions.getSortOrder().isPresent()
                && containsIgnoreCase(filterOptions.getSortOrder().get(), " desc ")){
            orderBy = String.format(" %s desc", orderBy);
        }
        return orderBy;
    }
    private static boolean containsIgnoreCase(String value, String sequence) {
        return value.toLowerCase().contains(sequence.toLowerCase());
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

    @Override
    public List<Post> getPostByAuthor(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery(
                    "from Post p join p.postCreatedBy u where u.username = :username", Post.class);
            query.setParameter("username", username);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Post", "username", username);
            }
            return query.list();
        }
    }

    // TODO - We should try this method for the 10 recently created posts when service is done
    @Override
    public List<Post> getPostByTimeStamp(Timestamp timestampCreated) {
        try (Session session = sessionFactory.openSession()) {
            Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
            Query<Post> query = session.createQuery(
                    "from Post where getTime <= :currentTime limit 10", Post.class);
            query.setParameter("timestamp_created", currentTime);
            List<Post> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException(
                        "Post", "timestampCreated", timestampCreated.toString());
            }
            return result.stream()
                    .filter(p -> p.getGetTime().isBefore(currentTime.toLocalDateTime()))
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
