package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@Controller
public class WebController {

    private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String showPage() {
        return "index";
    }

    @GetMapping(value = "/index")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/user")
    public String showUser(Model model, Principal principal) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return "user"; // имя представления
    }

    @GetMapping("/admin")
    public String showAdmin(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin"; // имя представления
    }

    @GetMapping(value = "/admin/new")
    public String newUser(ModelMap model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping(value = "/admin")
    public String create(@ModelAttribute("user") User user) {
        userService.saveAndFlush(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/delete")
    public String deleteUser(@RequestParam(value = "id") Long id) {

        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    public String editUser(@RequestParam(value = "id") Long id, ModelMap model) {
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }

    @PatchMapping("/admin/edit")
    public String updateUser(@RequestParam(value = "id") Long id, @ModelAttribute("user") User user) {
        userService.saveAndFlush(id, user);
        return "redirect:/admin";

    }
}
