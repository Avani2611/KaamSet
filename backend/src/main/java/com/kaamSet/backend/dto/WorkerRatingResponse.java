package com.kaamSet.backend.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerRatingResponse {

    private Double averageRating;
    private Long totalReviews;
}