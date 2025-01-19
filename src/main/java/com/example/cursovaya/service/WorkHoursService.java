package com.example.cursovaya.service;

import com.example.cursovaya.model.User;
import com.example.cursovaya.model.WorkHours;
import com.example.cursovaya.repository.WorkHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkHoursService {
    @Autowired
    WorkHoursRepository workHoursRepository;

    public boolean deleteWorkHours(User user){
        List<WorkHours> workHoursToDelete = findAllByUser(user);
        if (workHoursToDelete != null) {
            for (WorkHours wh: workHoursToDelete){
                workHoursRepository.delete(wh);
            }
            return true;
        }
        else{
            return false;
        }
    }

    public List<WorkHours> findAllByUser(User user){
        return workHoursRepository.findAllByUserId(user.getId());
    }
}
