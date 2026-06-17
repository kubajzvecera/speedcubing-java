package com.speedcubers.speedcubing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String country;

    @OneToMany(mappedBy = Solve_.COMPETITOR, cascade = CascadeType.ALL)
    private List<Solve> solves;

    @OneToMany(mappedBy = Result_.COMPETITOR, cascade = CascadeType.ALL)
    private List<Result> results;

    @OneToMany(mappedBy = Registration_.COMPETITOR, cascade = CascadeType.ALL)
    private List<Registration> registrations;
}
