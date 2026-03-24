package com.kaamSet.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerSearchResponse {

    private Long workerId;
    private String fullName;
    private String email;  
    private Double averageRating;
    private Long totalReviews;
    private Double distanceKm;
}