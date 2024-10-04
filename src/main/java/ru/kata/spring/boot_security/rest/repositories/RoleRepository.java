package ru.kata.spring.boot_security.rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.rest.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role saveAndFlush(Role role);

}
