package com.speedcubers.speedcubing.entity;

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

    @ManyToOne
    private Competitor competitor;

    @ManyToOne
    private Round round;
}
