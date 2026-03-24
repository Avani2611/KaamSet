package com.kaamSet.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerProfileResponse {

    private Long workerId;
    private String fullName;
    private String email;   
    private Double averageRating;
    private Long totalReviews;
}