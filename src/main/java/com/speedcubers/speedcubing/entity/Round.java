package com.speedcubers.speedcubing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Competition competition;

    @OneToMany(mappedBy = Solve_.ROUND, cascade = CascadeType.ALL)
    private List<Solve> solves;

    @OneToMany(mappedBy = Result_.ROUND, cascade = CascadeType.ALL)
    private List<Result> results;
}
