package com.example.cursovaya.controller;

import com.example.cursovaya.model.User;
import com.example.cursovaya.model.Vacation;
import com.example.cursovaya.service.UserService;
import com.example.cursovaya.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class VacationController {
    @Autowired
    VacationService vacationService;

    @Autowired
    UserService userService;

    @PostMapping("/vacationRequest")
    public String RequestVacation(@RequestParam String s,
                                  @RequestParam String e,
                                  Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username);

        System.out.println("Hello");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDateStart = LocalDate.parse(s, formatter);
        LocalDate localDateEnd = LocalDate.parse(e, formatter);

        LocalDateTime start = localDateStart.atStartOfDay();
        LocalDateTime end = localDateEnd.atStartOfDay();

        vacationService.requestVacation(user, start, end);
        return "redirect:/home";
    }

    @PostMapping("/confirmReject/{result}/{vacation_id}")
    public String confirmReject(@PathVariable int result,
                                @PathVariable Long vacation_id){
        Vacation vacation = vacationService.getVacation(vacation_id);
        vacation.setStatus(result);
        vacationService.saveVacation(vacation);
        return "redirect:/admin/userlist";
    }
}
