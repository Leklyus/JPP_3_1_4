package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {

    User findByUsername(String username);

    List<User> findAll();

    User getUserById(Long id);

    void saveAndFlush(Long id, User user);

    void saveAndFlush(User user);

    void deleteUserById(Long id);
}