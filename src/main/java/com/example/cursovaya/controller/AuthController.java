package com.example.cursovaya.controller;

import com.example.cursovaya.model.Role;
import com.example.cursovaya.model.User;
import com.example.cursovaya.model.Vacation;
import com.example.cursovaya.repository.UserRepository;
import com.example.cursovaya.service.UserService;
import com.example.cursovaya.service.VacationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;


@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            Model model){
        User user = userService.findByUsername(username);
        if (user != null && user.getPassword() == password){
            return "redirect:/home";
        }
        else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        // Получаем текущего авторизованного пользователя
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // Загружаем пользователя из базы данных по имени
        User user = userService.findByUsername(currentUsername);

        if (user != null) {
            // Добавляем данные пользователя в модель
            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("role", user.getRole().name());
        }

        // Переходим на страницу профиля
        return "user/profile";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               @RequestParam String role,
                               Model model) {

        if (userService.register(username, firstName, lastName, password, Role.valueOf(role))) {
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "register";
        }
    }

    @GetMapping("/home")
    public String homePage() {
        return "home"; // Главная страница после входа
    }

}
