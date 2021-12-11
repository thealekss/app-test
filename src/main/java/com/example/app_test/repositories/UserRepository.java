package com.example.app_test.repositories;

import com.example.app_test.entities.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository {
    Optional<User> findUserByEmail(String email);

    Optional<List<User>> findUsersByName(String name);

    User findUserById (int id);

    List<User> findAll();

    void deleteUserById (int id);


    void save (User user);
}
