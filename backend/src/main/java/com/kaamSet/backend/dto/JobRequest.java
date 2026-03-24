package com.kaamSet.backend.dto;

import com.kaamSet.backend.model.JobType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    // =========================
    // LOCATION FIELDS
    // =========================

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Area is required")
    private String area;

    @NotBlank(message = "Pincode is required")
    private String pincode;

    // =========================
    // JOB DETAILS
    // =========================

    @NotNull(message = "Job type is required")
    private JobType jobType;

    // Only required if jobType = CONTRACT
    @Min(value = 1, message = "Days must be at least 1")
    private Integer totalDays;
}