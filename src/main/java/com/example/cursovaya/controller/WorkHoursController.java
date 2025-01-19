package com.example.cursovaya.controller;

import com.example.cursovaya.model.User;
import com.example.cursovaya.model.WorkHours;
import com.example.cursovaya.repository.UserRepository;
import com.example.cursovaya.repository.WorkHoursRepository;
import com.example.cursovaya.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class WorkHoursController {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkHoursRepository workHoursRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/start")
    public String startWork(Model model) {
        // Получаем аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Ищем пользователя в базе данных
        User user = userService.findByUsername(username);

        if (user != null) {
            System.out.println("USER NOT NULL");
            // Проверяем, есть ли уже запись для пользователя
            List<WorkHours> workHoursList = workHoursRepository.findAllByUserId(user.getId());
            if (!workHoursList.isEmpty()) {
                System.out.println("Step 1");
                if (workHoursList.get(workHoursList.size() - 1).getEnd() == null) {
                    System.out.println("Step 2");
                    // Если запись найдена, то ничего не делаем (кнопка неактивна)
                    model.addAttribute("message", "Работа уже началась");
                    return "home";
                }
                else {
                    System.out.println("Step 3");
                    // Создаем новый объект WorkHours и записываем время старта
                    WorkHours workHours = new WorkHours();
                    workHours.setStart(LocalDateTime.now());
                    workHours.setUser(user);
                    workHoursRepository.save(workHours);
                    model.addAttribute("message", "Работа началась");
                    model.addAttribute("startButtonDisabled", true);
                    model.addAttribute("endButtonEnabled", true);
                    return "home";
                }
            }
            else{
                System.out.println("Step 3");
                // Создаем новый объект WorkHours и записываем время старта
                WorkHours workHours = new WorkHours();
                workHours.setStart(LocalDateTime.now());
                workHours.setUser(user);
                workHoursRepository.save(workHours);
                model.addAttribute("message", "Работа началась");
                model.addAttribute("startButtonDisabled", true);
                model.addAttribute("endButtonEnabled", true);
                return "home";
            }
        }
        return "error";
    }

    @PostMapping("/end")
    public String endWork(Model model) {
        // Получаем аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Ищем пользователя в базе данных
        User user = userService.findByUsername(username);

        if (user != null) {
            // Находим запись по пользователю
            List<WorkHours> workHoursList = workHoursRepository.findAllByUserId(user.getId());
            if (!workHoursList.isEmpty() && workHoursList.get(workHoursList.size() - 1).getEnd() == null) {
                // Записываем время окончания работы
                WorkHours workHours = workHoursList.get(workHoursList.size() - 1);
                workHours.setEnd(LocalDateTime.now());
                Duration duration = Duration.between(workHours.getStart(), workHours.getEnd());
                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;
                workHours.setDiff(String.format("%02d:%02d", hours, minutes));
                workHoursRepository.save(workHours);
                model.addAttribute("message", "Работа завершена");
                model.addAttribute("endButtonDisabled", true);
                return "home";
            } else {
                model.addAttribute("message", "Запись не найдена. Начните работу сначала.");
                return "home";
            }
        }
        return "error";
    }
}
