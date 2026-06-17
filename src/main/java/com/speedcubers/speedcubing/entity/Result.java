package com.speedcubers.speedcubing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Competitor competitor;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Round round;
}
