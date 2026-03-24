package com.kaamSet.backend.controller;

import com.kaamSet.backend.dto.ReviewRequest;
import com.kaamSet.backend.dto.ReviewResponse;
import com.kaamSet.backend.service.ReviewService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // ✅ GET WORKER REVIEWS (PAGINATED)
    @GetMapping("/worker/{workerId}")
    public ResponseEntity<Page<ReviewResponse>> getWorkerReviews(
            @PathVariable Long workerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                reviewService.getReviewsForWorker(workerId, pageable)
        );
    }

    // ✅ ADD REVIEW
    @PostMapping("/jobs/{jobId}/reviews")
    public ResponseEntity<?> addReview(
            @PathVariable Long jobId,
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication
            
    ) {
        reviewService.addReview(jobId, request, authentication);
        return ResponseEntity.ok().build();
    }

}
