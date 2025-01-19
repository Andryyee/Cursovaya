package com.example.cursovaya.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date; // Дата подачи
    private LocalDateTime start; // Дата начала
    private LocalDateTime end;   // Дата конца

    private int status; // -1 - отклонено, 0 - в обработке, 1 - принято

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
