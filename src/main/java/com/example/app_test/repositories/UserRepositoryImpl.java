package com.example.app_test.repositories;


import com.example.app_test.entities.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.swing.text.html.Option;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserRepositoryImpl implements UserRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return Optional.of(entityManager.createQuery("select u from User u where u.mail = :mail", User.class)
                .setParameter("mail", email)
                .getSingleResult());
    }

    @Override
    public Optional<List<User>> findUsersByName(String name) {
        return Optional.of(entityManager.createQuery("select u from User u where u.name = :name", User.class)
                .setParameter("name", name)
                .getResultList());
    }

    @Override
    public User findUserById(int id) throws NoResultException {

            return entityManager.createQuery("select u from User u where u.UserID = :id", User.class)
                    .setParameter("id", id).getSingleResult();

    }

    @Override
    public List<User> findAll() {

        return entityManager.createQuery("select u from User u ", User.class).getResultList();
    }

    @Override
    public void deleteUserById(int id) throws IllegalArgumentException {
        int result =   entityManager.createNativeQuery("delete from users where UserID = :id").setParameter("id", id).executeUpdate();
        if (result == 0) throw new IllegalArgumentException("User with id " + id + " not found");

    }


    @Override
    public void save(User user) throws ConstraintViolationException {

        entityManager.persist(user);
    }
}
