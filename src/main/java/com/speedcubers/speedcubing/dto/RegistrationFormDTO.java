package com.speedcubers.speedcubing.dto;

import lombok.Data;

import java.util.List;

@Data
public class RegistrationFormDTO {
    private Long competitionId;
    private List<Long> categoryIds;
}
