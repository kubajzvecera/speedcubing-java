package com.speedcubers.speedcubing.entity;

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
public class Competitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;

    @JsonManagedReference("competitor-solves")
    @OneToMany(mappedBy = Solve_.COMPETITOR, cascade = CascadeType.ALL)
    private List<Solve> solves;

    @JsonManagedReference("competitor-results")
    @OneToMany(mappedBy = Result_.COMPETITOR, cascade = CascadeType.ALL)
    private List<Result> results;
}
