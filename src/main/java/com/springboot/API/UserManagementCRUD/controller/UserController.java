package com.springboot.API.UserManagementCRUD.controller;

import java.util.HashMap;

import java.util.List;
import java.util.Map;


import com.springboot.API.UserManagementCRUD.exception.ResourceNotFoundException;
import com.springboot.API.UserManagementCRUD.model.User;
import com.springboot.API.UserManagementCRUD.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;

    /* Read Operation */

    @GetMapping("/users")
    public List<User> getAllUser() {
        LOGGER.info("Users Fletch successfully");
        return userRepository.findAll();
    }

    /* Fletch users according to firstName */

    @GetMapping("/users/searchFirst/{firstName}")
    public List<User> getUserByFirstName(@PathVariable(value = "firstName") String FirstName)
            throws ResourceNotFoundException {
        LOGGER.info("Users fetched with firstname ");
        return (List<User>) userRepository.findByFirstName(FirstName);
    }

    /* Fletch users according to lastName */

    @GetMapping("/users/searchLast/{lastName}")
    public List<User> getUserByLastName(@PathVariable(value = "lastName") String LastName)
            throws ResourceNotFoundException {
        LOGGER.info("Users fetched with lastname ");
        return (List<User>) userRepository.findByLastName(LastName);
    }

    /* Fletch users according to ID */

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        return ResponseEntity.ok().body(user);
    }

    /* Create Operation */

    @PostMapping("/users")
    public User createUser(@Validated @RequestBody User user) {
        LOGGER.info("Request received to create user [{}]", user.getFirstName());
        User save = userRepository.save(user);
        LOGGER.info("User [{}] saved", user.getFirstName());
        return save;
    }

    /* Update Operation */

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                           @Validated @RequestBody User userDetails) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        user.setEmailId(userDetails.getEmailId());
        user.setLastName(userDetails.getLastName());
        user.setFirstName(userDetails.getFirstName());
        user.setUserName(userDetails.getUserName());
        final User updatedUser = userRepository.save(user);
        LOGGER.info("User updated with id [{}] username [{}]", user.getId(), user.getUserName());
        return ResponseEntity.ok(updatedUser);
    }

    /* Delete Operation */

    @DeleteMapping("/users/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId)
            throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        LOGGER.info("User Deleted username[{}]", user.getUserName());
        return response;
    }
}
