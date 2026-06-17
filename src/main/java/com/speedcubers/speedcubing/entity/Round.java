package com.speedcubers.speedcubing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int roundNumber;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Competition competition;

    @OneToMany(mappedBy = Solve_.ROUND, cascade = CascadeType.ALL)
    private List<Solve> solves;

    @OneToMany(mappedBy = Result_.ROUND, cascade = CascadeType.ALL)
    private List<Result> results;
}
