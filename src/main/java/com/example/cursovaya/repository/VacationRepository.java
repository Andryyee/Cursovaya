package com.example.cursovaya.repository;

import com.example.cursovaya.model.User;
import com.example.cursovaya.model.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
    List<Vacation> findByUser (User user);
    boolean existsByUserAndStatus(User user, int status);
    Optional<Vacation> findById(Long id);
}
