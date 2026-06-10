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
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer bestTime;
    private Double averageTime;
    private int rank;

    @JsonBackReference("competitor-results")
    @ManyToOne
    @JoinColumn(name = "competitor_id")
    private Competitor competitor;

    @JsonBackReference("round-results")
    @ManyToOne
    @JoinColumn(name = "round_id")
    private Round round;
}
