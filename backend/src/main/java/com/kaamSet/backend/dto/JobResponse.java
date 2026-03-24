package com.kaamSet.backend.dto;

import com.kaamSet.backend.model.JobStatus;
import com.kaamSet.backend.model.JobType;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JobResponse {

    private Long id;

    private String title;
    private String description;

    private String city;
    private String area;
    private String pincode;

    private JobType jobType;
    private Integer totalDays;

    private Double oneTimePrice;
    private Double perDaySalary;

    private JobStatus status;

    private UserResponse user;
    private UserResponse worker;

    private LocalDateTime createdAt;
}