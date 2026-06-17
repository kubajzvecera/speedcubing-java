package com.speedcubers.speedcubing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Solve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int attemptNumber;
    private int timeMs;

    private String penalty;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Competitor competitor;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Round round;
}
