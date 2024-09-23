package ru.kata.spring.boot_security.demo.services;


import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {


    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Заполнение таблицы пользователями
        Role role1 = new Role();
        role1.setName("ROLE_USER");
        roleRepository.saveAndFlush(role1);

        Role role2 = new Role();
        role2.setName("ROLE_ADMIN");
        roleRepository.saveAndFlush(role2);

        User user1 = new User();
        user1.setUsername("user");
        user1.setName("Bob");
        user1.setSurname("Mikly");
        user1.setAge((byte) 30);
        user1.setPassword("$2a$12$8u5ndHEv77jayLkGs/kPaO6GKrHEIfrG97mcUxYzLx1t/dogR5GGO");

        java.util.Collection<Role> roles1 = new ArrayList<>();
        roles1.add(role1);
        user1.setRoles(roles1);

        userRepository.saveAndFlush(user1);

        User user2 = new User();
        user2.setUsername("admin");
        user2.setName("Jhon");
        user2.setSurname("Torris");
        user2.setAge((byte) 26);
        user2.setPassword("$2a$12$hMLiAA1JSHG7g41DLImov.X9jkX55tVEChVy5RaDtkoE58eeamlhW");

        java.util.Collection<Role> roles2 = new ArrayList<>();
        roles2.add(role2);
        user2.setRoles(roles2);

        userRepository.saveAndFlush(user2);



    }
}