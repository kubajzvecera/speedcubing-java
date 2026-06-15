package com.speedcubers.speedcubing.dto;

import lombok.Data;

@Data
public class RoundFormDTO {
    private String name;
    private int roundNumber;
    private Long competitionId;
}
