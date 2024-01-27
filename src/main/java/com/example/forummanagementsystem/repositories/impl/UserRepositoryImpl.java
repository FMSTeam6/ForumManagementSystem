package com.example.forummanagementsystem.repositories.impl;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UserStatusCannotBeChangedException;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.repositories.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    //TODO
    @Override
    public List<User> getAll() {
        try(Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("from User",User.class);
            return query.list();
        }

    }

    @Override
    public User getById(int id) {
        try(Session session = sessionFactory.openSession()) {
            User user = session.get(User.class,id);
            if (user == null){
                throw new EntityNotFoundException("User",id);
            }
            return user;
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(
                    "from User where username = :username", User.class);
            query.setParameter("username", username);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", username);
            }
            return result.get(0);
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(
                    "from User where email = :email", User.class);
            query.setParameter("email", email);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return result.get(0);
        }
    }

    @Override
    public User getByFirstName(String firstName) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(
                    "from User where first_name = :first_name", User.class);
            query.setParameter("first_name", firstName);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "firstName", firstName);
            }
            return result.get(0);
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void banUser(User user) {
        try(Session session = sessionFactory.openSession()){

            User userToBan = session.get(User.class, user.getId());

            Query<User> query = session.createQuery(
                    "from User where User.isBanned = true",User.class
            );
            List<User> result = query.list();
            if (result.contains(userToBan)){
                throw new UserStatusCannotBeChangedException(userToBan.getUsername(),"banned.");
            }
            session.beginTransaction();
            userToBan.setBanned(true);
            session.saveOrUpdate(userToBan);
            session.getTransaction().commit();

        }

    }

    @Override
    public void unBanUser(User user) {
        try(Session session = sessionFactory.openSession()) {

            User userToUnBan = session.get(User.class, user.getId());

            Query<User> query = session.createQuery(
                    "from User where User.isBanned = false", User.class
            );
            List<User> result = query.list();
            if (result.contains(userToUnBan)) {
                throw new UserStatusCannotBeChangedException(userToUnBan.getUsername(),"unbanned.");
            }
            session.beginTransaction();
            userToUnBan.setBanned(false);
            session.saveOrUpdate(userToUnBan);
            session.getTransaction().commit();
        }
    }

    @Override
    public void giveAdminRights(User user) {
        try(Session session = sessionFactory.openSession()) {

            User userToReceiveAdminRights = session.get(User.class, user.getId());

            Query<User> query = session.createQuery(
                    "from User where User.isAdmin = true", User.class
            );
            List<User> result = query.list();
            if (result.contains(userToReceiveAdminRights)) {
                throw new UserStatusCannotBeChangedException(userToReceiveAdminRights.getUsername(),"admin.");
            }
            session.beginTransaction();
            userToReceiveAdminRights.setAdmin(true);
            session.saveOrUpdate(userToReceiveAdminRights);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteAdminRights(User user) {
        try(Session session = sessionFactory.openSession()) {

            User userToRemoveAdminRights = session.get(User.class, user.getId());

            Query<User> query = session.createQuery(
                    "from User where User.isAdmin = false", User.class
            );
            List<User> result = query.list();
            if (result.contains(userToRemoveAdminRights)) {
                throw new UserStatusCannotBeChangedException(userToRemoveAdminRights.getUsername(),"not an admin.");
            }
            session.beginTransaction();
            userToRemoveAdminRights.setAdmin(false);
            session.saveOrUpdate(userToRemoveAdminRights);
            session.getTransaction().commit();
        }
    }
}
