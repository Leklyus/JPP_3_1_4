package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Transactional
    public void saveAndFlush(Long id, User user) {
        User baseUser = userRepository.getUserById(id);
        baseUser.setUsername(user.getUsername());
        baseUser.setName(user.getName());
        baseUser.setSurname(user.getSurname());
        baseUser.setAge(user.getAge());
        userRepository.saveAndFlush(baseUser);
    }

    @Transactional
    public void saveAndFlush(User user) {
        userRepository.saveAndFlush(user);
    }

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
                user.getPassword(), mapRolesToAuthories(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthories(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }
}
