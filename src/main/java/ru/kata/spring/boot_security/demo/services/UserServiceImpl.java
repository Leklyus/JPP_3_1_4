package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    @Transactional
    public void saveAndFlush(Long id, User user) {
        User baseUser = userRepository.getUserById(id);
        baseUser.setUsername(user.getUsername());
        baseUser.setName(user.getName());
        baseUser.setSurname(user.getSurname());
        baseUser.setAge(user.getAge());
        baseUser.setPassword(user.getPassword());
        baseUser.setRoles(user.getRoles());
        userRepository.saveAndFlush(baseUser);
    }

    @Override
    @Transactional
    public void saveAndFlush(User user) {
        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteUserById(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found.", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.getRoles());
    }
}
