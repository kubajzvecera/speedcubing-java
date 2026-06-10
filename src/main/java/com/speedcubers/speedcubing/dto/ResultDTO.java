package com.speedcubers.speedcubing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultDTO {
    private Long competitorId;
    private String competitorName;
    private Integer bestTime;
    private Double averageTime;
    private int rank;
}
