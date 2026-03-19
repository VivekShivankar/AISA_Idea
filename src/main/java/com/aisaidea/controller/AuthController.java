package com.aisaidea.controller;

import com.aisaidea.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        Model model) {
        if (error  != null) model.addAttribute("error",  "Invalid username or password. Please try again.");
        if (logout != null) model.addAttribute("msg",    "You have been logged out successfully.");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam(defaultValue = "") String fullName,
                             RedirectAttributes ra) {
        try {
            userService.register(username, email, password, fullName);
            ra.addFlashAttribute("msg", "Account created successfully! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}
