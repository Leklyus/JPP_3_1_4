package ru.kata.spring.boot_security.rest.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.rest.models.User;
import ru.kata.spring.boot_security.rest.services.RoleService;
import ru.kata.spring.boot_security.rest.services.UserService;

import java.security.Principal;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/user")
public class RestUserController {

    private final UserService userService;
    private final RoleService roleService;

    public RestUserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/currentUser")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        User curUser = userService.findByEmail(principal.getName());
        return new ResponseEntity<>(curUser, HttpStatus.OK);
    }
}
