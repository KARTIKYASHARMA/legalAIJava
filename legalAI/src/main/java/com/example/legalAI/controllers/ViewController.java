package com.example.legalAI.controllers;

import com.example.legalAI.entities.Users;
import com.example.legalAI.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class ViewController {

    private final UserService userService;

    public ViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/auth/register")
    public String registerUser(@ModelAttribute Users user) {
        try {
            userService.registerUser(user);
            return "redirect:/login?registered";
        } catch (Exception e) {
            return "redirect:/register?error";
        }
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/chat";
    }


    @GetMapping("/form")
    public String formPage(Model model, Principal principal) {
        String name = (principal != null) ? principal.getName() : "Guest";
        model.addAttribute("username", name);
        return "form";
    }

}
