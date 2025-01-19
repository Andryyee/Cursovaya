package com.example.cursovaya.controller;

import com.example.cursovaya.model.Role;
import com.example.cursovaya.model.User;
import com.example.cursovaya.model.Vacation;
import com.example.cursovaya.model.WorkHours;
import com.example.cursovaya.repository.UserRepository;
import com.example.cursovaya.repository.WorkHoursRepository;
import com.example.cursovaya.service.UserService;
import com.example.cursovaya.service.VacationService;
import com.example.cursovaya.service.WorkHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkHoursRepository whRepository;

    @Autowired
    private WorkHoursService workHoursService;
    @Autowired
    private UserService userService;
    @Autowired
    private VacationService vacationService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Страница со списком пользователей, доступна только для администраторов
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/userlist")
    public String showUserList(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/userlist";
    }

    @GetMapping("/createNewUser")
    public String createNewUser(){
        return "admin/createNewUser";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/deleteUser/{username}")
    public String deleteUser(@PathVariable String username) {
        User user = userService.findByUsername(username);
        workHoursService.deleteWorkHours(user);
        vacationService.deleteVacation(user);
        userService.deleteUser(username);
        return "redirect:/admin/userlist"; // После удаления перенаправляем обратно на список пользователей
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/userWorkHours/{username}")
    public String userWorkHours(@PathVariable String username,Model model) {
        User user = userService.findByUsername(username);
        List<WorkHours> userWorkHours = workHoursService.findAllByUser(user);
        model.addAttribute("userWorkHours", userWorkHours);
        return "admin/userWorkHours";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/vacationList/{username}")
    public String vacationList(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        List<Vacation> vacations = vacationService.getUserVacations(user);
        model.addAttribute("vacations", vacations);
        return "admin/vacationList";
    }

    @PostMapping("createNewUser")
    public String createNewUserComplite(@RequestParam String firstName,
                                        @RequestParam String lastName,
                                        @RequestParam String username,
                                        @RequestParam String password,
                                        @RequestParam String confirmPassword,
                                        @RequestParam String role){
        if (userRepository.findByUsername(username).isPresent()) {
            return "/admin/createNewUser"; // Пользователь с таким email уже существует
        }

        User user = new User();
        WorkHours wh = new WorkHours();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.valueOf(role));
        userRepository.save(user);
        return "redirect:/admin/userlist";
    }
}
