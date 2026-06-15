package com.speedcubers.speedcubing.dto;

import lombok.Data;

@Data
public class SolveDTO {
    private Long competitorId;
    private int timeMs;
    private String penalty;
    private Long competitionId;
}
