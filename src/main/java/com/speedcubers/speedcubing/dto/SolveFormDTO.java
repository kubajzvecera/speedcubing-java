package com.speedcubers.speedcubing.dto;

import lombok.Data;

@Data
public class SolveFormDTO {
    private Long competitorId;
    private int timeMs;
    private String penalty;
    private Long competitionId;
}
