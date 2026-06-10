package com.speedcubers.speedcubing.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference("competitor-solves")
    @ManyToOne
    @JoinColumn(name = "competitor_id")
    private Competitor competitor;

    @JsonBackReference("round-solves")
    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;
}
