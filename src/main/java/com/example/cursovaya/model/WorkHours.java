package com.example.cursovaya.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class WorkHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime start;
    private LocalDateTime end;
    private String diff;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public WorkHours() {}
}
