package com.kaamSet.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private int rating;
    private String comment;

    private Long userId;       
    private String userName;

    private LocalDateTime createdAt;
}