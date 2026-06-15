package com.speedcubers.speedcubing.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Competitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String country;

    @JsonManagedReference("competitor-solves")
    @OneToMany(mappedBy = "competitor", cascade = CascadeType.ALL)
    private List<Solve> solves;

    @JsonManagedReference("competitor-results")
    @OneToMany(mappedBy = "competitor", cascade = CascadeType.ALL)
    private List<Result> results;

    @JsonManagedReference("competitor-registrations")
    @OneToMany(mappedBy = "competitor", cascade = CascadeType.ALL)
    private List<Registration> registrations;
}
