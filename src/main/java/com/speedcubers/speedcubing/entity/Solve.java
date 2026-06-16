package com.speedcubers.speedcubing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Solve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int attemptNumber;
    private int timeMs;

    private String penalty;

    @ManyToOne
    @JoinColumn(name = "competitor_id")
    private Competitor competitor;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;
}
