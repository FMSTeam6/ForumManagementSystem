package com.example.forummanagementsystem.repositories.impl;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.repositories.CommentRepository;
import com.example.forummanagementsystem.repositories.PostRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;
    private final PostRepository postRepository;
    @Autowired
    public CommentRepositoryImpl(SessionFactory sessionFactory, PostRepository postRepository) {
        this.sessionFactory = sessionFactory;
        this.postRepository = postRepository;
    }

    @Override
    public List<Comment> getAllCommentFromPost(int postId) {
        try(Session session = sessionFactory.openSession()) {
            Post post = postRepository.getPostById(postId);
            Query<Comment> query = session.createQuery("from Comment WHERE id =: post",Comment.class);
            query.setParameter("post",post);
            List<Comment> result = query.list();
            if (result.isEmpty()){
                throw new EntityNotFoundException("Comment",postId);
            }
            return result;
        }
    }

    @Override
    public List<Comment> getAll() {
        try(Session session = sessionFactory.openSession()) {
            Query<Comment> query = session.createQuery("from Comment",Comment.class);
            return query.list();
        }
    }

    @Override
    public Comment getById(int id) {
        try(Session session = sessionFactory.openSession()) {
            Comment comment = session.get(Comment.class,id);
            if (comment == null){
                throw new EntityNotFoundException("Comment",id);
            }
            return comment;
        }
    }

    @Override
    public void createComment(Comment comment) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.persist(comment);
            session.getTransaction().commit();
        }

    }

    @Override
    public void updateComment(Comment comment) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(comment);
            session.getTransaction().commit();;
        }

    }

    @Override
    public void deleteComment(int id) {
        Comment commitToDelete = getById(id);
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(commitToDelete);
            session.getTransaction().commit();
        }

    }

    @Override
    public User getAuthorComment(int authorId) {
        try(Session session = sessionFactory.openSession()) {
            User user = session.get(User.class,authorId);
            if (user == null){
                throw new EntityNotFoundException("User",authorId);
            }
            return user;
        }
    }
}
