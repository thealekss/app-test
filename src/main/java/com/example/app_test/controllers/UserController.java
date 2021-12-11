package com.example.app_test.controllers;

import com.example.app_test.entities.User;
import com.example.app_test.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {

        List<User> users =  userRepository.findAll();

        logger.info("Request for retrieving all users is processed successfully");
        return users;
    }

    @PostMapping("/users")
    @Transactional
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        userRepository.save(user);
        logger.info("Request for creating {} is processed successfully", user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> retrieveUserById(@PathVariable int id) {
        try {
            User user = userRepository.findUserById(id);
            logger.info("Request for retrieving user by id is processed successfully");
            return ResponseEntity.ok(user);
        }
        catch (NoResultException exc) {
            logger.info("User with id {} not found ", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }


    }

    @PatchMapping("/users/{id}")
    @Transactional
    public ResponseEntity<User> modifyUser(@PathVariable int id, @RequestBody Map<String, Object> updates) {
        try {
            User user = userRepository.findUserById(id);
            updates.forEach(
                    (key, value) -> {
                        Field field = ReflectionUtils.findField(User.class, key);
                        field.setAccessible(true);
                        ReflectionUtils.setField(field, user, (String) value);
                    }
            );
            logger.info("Request for updating user  is processed successfully");
            //userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (NoResultException exc) {
            logger.info("User with id {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        catch (NullPointerException exc) {
            logger.info("There is a mistake in request body. Some of the fields are not acceptable ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }

    @DeleteMapping("/users/{id}")
    @Transactional
    public ResponseEntity<User> deleteUser(@PathVariable int id) {
        try {
            userRepository.deleteUserById(id);
            logger.info("User with id {} is removed", id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (IllegalArgumentException exc) {
            logger.info(exc.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }


    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
