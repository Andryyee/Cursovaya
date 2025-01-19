package com.example.cursovaya.repository;

import com.example.cursovaya.model.WorkHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkHoursRepository extends JpaRepository<WorkHours, Long> {
    Optional<WorkHours> findByUserId(Long userId);
    List<WorkHours> findAllByUserId(Long userId);
}
