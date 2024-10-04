package ru.kata.spring.boot_security.rest.services;

import ru.kata.spring.boot_security.rest.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();
}
