package ru.kata.spring.boot_security.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.rest.models.User;
import ru.kata.spring.boot_security.rest.repositories.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
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
        baseUser.setFirstname(user.getFirstname());
        baseUser.setLastname(user.getLastname());
        baseUser.setAge(user.getAge());

        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        baseUser.setPassword(encryptedPassword);

        baseUser.setRoles(user.getRoles());
        userRepository.saveAndFlush(baseUser);
    }

    @Override
    @Transactional
    public void saveAndFlush(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteUserById(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = findByUsername(username);
        User user = findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with email: '%s' not found.", email));
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), user.getRoles());
    }
}
