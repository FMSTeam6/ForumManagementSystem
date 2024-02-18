package com.example.forummanagementsystem.repositories.impl;

import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UserStatusCannotBeChangedException;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.filters.FilterOptions;
import com.example.forummanagementsystem.models.filters.SearchUser;
import com.example.forummanagementsystem.repositories.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
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
                    "from User where firstName = :first_name", User.class);
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
        try (Session session = sessionFactory.openSession()) {

            User userToBan = session.get(User.class, user.getId());

            Query<User> query = session.createQuery(
                    "from User where isBanned = :is_banned", User.class);
            query.setParameter("is_banned", true);
            List<User> result = query.list();
            if (result.contains(userToBan)) {
                throw new UserStatusCannotBeChangedException(userToBan.getUsername(), "banned.");
            }
            session.beginTransaction();
            userToBan.setBanned(true);
            session.saveOrUpdate(userToBan);
            session.getTransaction().commit();

        }

    }

    @Override
    public void unBanUser(User user) {
        try (Session session = sessionFactory.openSession()) {

            User userToUnBan = session.get(User.class, user.getId());

            Query<User> query = session.createQuery(
                    "from User where isBanned = :is_banned", User.class);
            query.setParameter("is_banned", false);
            List<User> result = query.list();
            if (result.contains(userToUnBan)) {
                throw new UserStatusCannotBeChangedException(userToUnBan.getUsername(), "unbanned.");
            }
            session.beginTransaction();
            userToUnBan.setBanned(false);
            session.saveOrUpdate(userToUnBan);
            session.getTransaction().commit();
        }
    }

    @Override
    public void giveAdminRights(User user) {
        try (Session session = sessionFactory.openSession()) {

            User userToReceiveAdminRights = session.get(User.class, user.getId());

            Query<User> query = session.createQuery(
                    "from User where isAdmin = :is_admin", User.class);
            query.setParameter("is_admin", true);
            List<User> result = query.list();
            if (result.contains(userToReceiveAdminRights)) {
                throw new UserStatusCannotBeChangedException(userToReceiveAdminRights.getUsername(), "admin.");
            }
            session.beginTransaction();
            userToReceiveAdminRights.setAdmin(true);
            session.saveOrUpdate(userToReceiveAdminRights);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteAdminRights(User user) {
        try (Session session = sessionFactory.openSession()) {

            User userToRemoveAdminRights = session.get(User.class, user.getId());

            Query<User> query = session.createQuery(
                    "from User where isAdmin = :is_admin", User.class);
            query.setParameter("is_admin", false);
            List<User> result = query.list();
            if (result.contains(userToRemoveAdminRights)) {
                throw new UserStatusCannotBeChangedException(userToRemoveAdminRights.getUsername(), "not an admin.");
            }
            session.beginTransaction();
            userToRemoveAdminRights.setAdmin(false);
            session.saveOrUpdate(userToRemoveAdminRights);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteUser(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getById(id));
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getAll(SearchUser searchUser) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryString = new StringBuilder(" from User ");
            ArrayList<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();
            searchUser.getUsername().ifPresent(value -> {
                filters.add(" username like: username ");
                params.put("username", String.format("%%%s%%", value));
            });
            searchUser.getEmail().ifPresent(value -> {
                filters.add(" email like: email ");
                params.put("email", String.format("%%%s%%", value));
            });
            searchUser.getFirstName().ifPresent(value -> {
                filters.add(" first_name like: firstName ");
                params.put("firstName", String.format("%%%s%%", value));
            });
            searchUser.getLastName().ifPresent(value -> {
                filters.add(" last_name =: lastName ");
                params.put("lastName", String.format("%%%s%%", value));
            });
            if (!filters.isEmpty()) {
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }
            queryString.append(generateOrderBy(searchUser));
            Query<User> query = session.createQuery(queryString.toString(), User.class);
            query.setProperties(params);
            return query.list();
        }
    }

    private String generateOrderBy(SearchUser searchUser) {
        if (searchUser.getSortBy().isEmpty()) {
            return "";
        }
        String orderBy = "";
        switch (searchUser.getSortBy().get()) {
            case "username":
                orderBy = "username";
                break;
            case "email":
                orderBy = "email";
                break;
            case "firstName":
                orderBy = "first_name";
                break;
            case "lastName":
                orderBy = "last_name";
                break;
        }
        orderBy = String.format(" orderBy %s ", orderBy);
        if (searchUser.getSortOrder().isPresent()
                && containsIgnoreCase(searchUser.getSortOrder().get(), " desc ")) {
            orderBy = String.format(" %s desc", orderBy);
        }
        return orderBy;
    }

    private static boolean containsIgnoreCase(String value, String sequence) {
        return value.toLowerCase().contains(sequence.toLowerCase());
    }
}
