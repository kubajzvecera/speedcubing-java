package com.speedcubers.speedcubing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SolveDTO {
    private Long roundId;
    private Long competitorId;
    private int timeMs;
    private String penalty;
}
