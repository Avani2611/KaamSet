package com.kaamSet.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkerSkillRequest {

    @NotNull(message = "Skill ID is required")
    private Long skillId;

    @Min(value = 0, message = "Experience must be 0 or more")
    @Max(value = 60, message = "Experience cannot exceed 60 years")
    private int experienceYears;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}