package com.example.cursovaya.controller;

import com.example.cursovaya.model.User;
import com.example.cursovaya.model.Vacation;
import com.example.cursovaya.service.UserService;
import com.example.cursovaya.service.VacationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private VacationService vacationService;

    @Autowired
    private UserService userService;

    @GetMapping("/vacation")
    public String viewVacation(){
        return "user/requestVacation";
    }

    @GetMapping("/")
    public String home(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);
        return "home";
    }

    @GetMapping("/logout")
    public String logout() {
        // Можно явно очистить SecurityContext, но Spring Security сделает это автоматически
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("User " + auth.getName() + " logged out.");
        }
        return "redirect:/login"; // Перенаправление на страницу входа
    }
}
