package com.speedcubers.speedcubing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate date;
    private String location;
    private LocalDate endDate;

    public List<Category> getCategories() {
        if (rounds == null) return List.of();
        return rounds.stream()
            .map(Round::getCategory)
            .filter(Objects::nonNull)
            .distinct()
            .toList();
    }

    @OneToMany(mappedBy = Registration_.COMPETITION, cascade = CascadeType.ALL)
    private List<Registration> registrations;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    private List<Round> rounds;
}
