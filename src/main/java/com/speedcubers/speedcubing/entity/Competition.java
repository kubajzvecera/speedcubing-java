package com.speedcubers.speedcubing.entity;

import com.speedcubers.speedcubing.entity.Registration_;
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
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate date;
    private String location;
    private LocalDate endDate;

    @OneToMany(mappedBy = Registration_.COMPETITION, cascade = CascadeType.ALL)
    private List<Registration> registrations;

    @OneToMany(mappedBy = Round_.COMPETITION, cascade = CascadeType.ALL)
    private List<Round> rounds;
}
