package com.speedcubers.speedcubing.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompetitionFormDTO {
    private String name;
    private LocalDate date;
    private String location;
    private LocalDate endDate;
}
