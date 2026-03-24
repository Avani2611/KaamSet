package com.kaamSet.backend.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class WorkerReviewResponse {

    private int rating;
    private String comment;
    private String userName;
    private LocalDateTime createdAt;
}