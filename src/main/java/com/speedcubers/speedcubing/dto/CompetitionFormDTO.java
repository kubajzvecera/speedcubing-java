package com.speedcubers.speedcubing.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CompetitionFormDTO {
    private String name;
    private LocalDate date;
    private String location;
    private LocalDate endDate;
    private Long organizerId;
    private List<Long> categoryIds;
}
