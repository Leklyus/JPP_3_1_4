package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
public class WebController {

    private final UserService userService;
    private final RoleService roleService;

    public WebController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = {"/", "/index"})
    public String showPage() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user")
    public String showUser(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        model.addAttribute("currentUser", userService.findByEmail(currentUser.getUsername()));
        return "user"; // имя представления
    }

    @GetMapping("/admin")
    public String showAdmin(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        model.addAttribute("currentUser", userService.findByEmail(currentUser.getUsername()));

        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());
        return "admin"; // имя представления
    }

    @PostMapping(value = "/admin/new")
    public String create(@ModelAttribute("user") User user) {
        userService.saveAndFlush(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam(value = "id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/edit")
    public String updateUser(@RequestParam(value = "id") Long id, @ModelAttribute("user") User user) {
        userService.saveAndFlush(id, user);
        return "redirect:/admin";

    }

}
