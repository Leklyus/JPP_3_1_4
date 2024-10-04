package ru.kata.spring.boot_security.rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.rest.models.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User getUserById(Long id);

    List<User> findAll();

    User saveAndFlush(User user);

    void deleteUserById(Long id);
}
