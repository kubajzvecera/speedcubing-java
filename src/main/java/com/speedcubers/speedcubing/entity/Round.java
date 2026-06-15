package com.speedcubers.speedcubing.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int roundNumber;

    @JsonBackReference("category-rounds")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonManagedReference("round-solves")
    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<Solve> solves;

    @JsonManagedReference("round-results")
    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<Result> results;
}
