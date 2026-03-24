package com.kaamSet.backend.controller;

import com.kaamSet.backend.dto.ReviewResponse;
import com.kaamSet.backend.dto.WorkerProfileResponse;
import com.kaamSet.backend.dto.WorkerRatingResponse;
import com.kaamSet.backend.dto.WorkerSearchResponse;
import com.kaamSet.backend.service.WorkerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    // -------------------- WORKER PROFILE --------------------

    @GetMapping("/{workerId}/profile")
    public ResponseEntity<WorkerProfileResponse> getWorkerProfile(
            @PathVariable Long workerId) {
        return ResponseEntity.ok(workerService.getWorkerProfile(workerId));
    }

    // -------------------- WORKER REVIEWS --------------------

    @GetMapping("/{workerId}/reviews")
    public ResponseEntity<Page<ReviewResponse>> getWorkerReviews(
            @PathVariable Long workerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                workerService.getWorkerReviews(workerId, pageable)
        );
    }

    // -------------------- WORKER RATING --------------------

    @GetMapping("/{workerId}/rating")
    public ResponseEntity<WorkerRatingResponse> getWorkerRating(
            @PathVariable Long workerId) {
        return ResponseEntity.ok(workerService.getWorkerRating(workerId));
    }

    // -------------------- SEARCH WORKERS (MAIN ENDPOINT) --------------------

@GetMapping("/search")
public ResponseEntity<Page<WorkerSearchResponse>> searchWorkers(
        @RequestParam Long skillId,
        @RequestParam(defaultValue = "rating") String sort,
        @RequestParam(required = false) Double lat,
        @RequestParam(required = false) Double lng,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
) {
    return ResponseEntity.ok(
            workerService.getWorkersBySkill(
                    skillId, page, size, sort, lat, lng
            )
    );
}





}
