package com.speedcubers.speedcubing.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime registrationDatetime;

    @JsonBackReference("competition-registrations")
    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @JsonBackReference("competitor-registrations")
    @ManyToOne
    @JoinColumn(name = "competitor_id")
    private Competitor competitor;

    @ManyToMany
    @JoinTable(
        name = "registration_category",
        joinColumns = @JoinColumn(name = "registration_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
}
