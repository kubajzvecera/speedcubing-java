package com.speedcubers.speedcubing.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompetitorFormDTO {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String country;
}
