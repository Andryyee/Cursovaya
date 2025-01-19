package com.example.cursovaya.service;

import com.example.cursovaya.model.User;
import com.example.cursovaya.model.Vacation;
import com.example.cursovaya.repository.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VacationService {
    @Autowired
    private VacationRepository vacationRepository;

    public Vacation requestVacation(User user, LocalDateTime start, LocalDateTime end) {
        if (vacationRepository.existsByUserAndStatus(user, 0)) {
            throw new IllegalStateException("У вас уже есть незавершенный запрос на отпуск.");
        }

        Vacation vacation = new Vacation();
        vacation.setDate(LocalDateTime.now());
        vacation.setStart(start);
        vacation.setEnd(end);
        vacation.setStatus(0); // В обработке
        vacation.setUser (user);

        return vacationRepository.save(vacation);
    }

    public List<Vacation> getUserVacations(User user) {
        return vacationRepository.findByUser (user);
    }

    public Vacation getVacation(Long id){
        return vacationRepository.findById(id).orElseThrow();
    }

    public void deleteVacation(User user){
        for (Vacation vacation: vacationRepository.findByUser(user)) {
            vacationRepository.delete(vacation);
        }
    }

    public void saveVacation(Vacation vacation){
        vacationRepository.save(vacation);
    }
}
