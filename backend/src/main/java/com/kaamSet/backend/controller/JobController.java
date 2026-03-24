package com.kaamSet.backend.controller;

import com.kaamSet.backend.dto.AcceptJobRequest;
import com.kaamSet.backend.dto.JobRequest;
import com.kaamSet.backend.dto.JobResponse;
import com.kaamSet.backend.model.Job;
import com.kaamSet.backend.service.JobService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // =========================
    // USER CREATES JOB
    // =========================
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody JobRequest request) {

        return ResponseEntity.ok(
                jobService.mapToResponse(jobService.createJob(request))
        );
    }

    // =========================
    // WORKER VIEWS AVAILABLE JOBS
    // =========================
    @GetMapping("/available")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<Page<JobResponse>> getAvailableJobs(Pageable pageable) {

        return ResponseEntity.ok(
                jobService.getAvailableJobsForWorker(pageable)
        );
    }

    // =========================
    // WORKER ACTIVE JOBS
    // =========================
    @GetMapping("/worker/active")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<Page<JobResponse>> getWorkerActiveJobs(Pageable pageable) {

        return ResponseEntity.ok(
                jobService.getWorkerActiveJobs(pageable)
        );
    }

    // =========================
    // USER JOB HISTORY
    // =========================
    @GetMapping("/user/history")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<JobResponse>> getUserHistory(Pageable pageable) {

        return ResponseEntity.ok(
                jobService.getUserJobHistory(pageable)
        );
    }

    // =========================
    // WORKER JOB HISTORY
    // =========================
    @GetMapping("/worker/history")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<Page<JobResponse>> getWorkerHistory(Pageable pageable) {

        return ResponseEntity.ok(
                jobService.getWorkerJobHistory(pageable)
        );
    }

    // =========================
    // WORKER ACCEPTS JOB
    // =========================
    @PutMapping("/{jobId}/accept")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<?> acceptJob(
            @PathVariable Long jobId,
            @RequestBody AcceptJobRequest request,
            Authentication authentication
    ) {

        jobService.acceptJob(
                jobId,
                request.getOneTimePrice(),
                request.getPerDaySalary(),
                authentication
        );

        return ResponseEntity.ok("Job accepted successfully");
    }

    // =========================
    // WORKER COMPLETES JOB
    // =========================
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<Void> completeJob(
            @PathVariable Long id,
            Authentication authentication
    ) {

        jobService.completeJob(id, authentication);
        return ResponseEntity.ok().build();
    }

    // =========================
    // USER CANCELS JOB
    // =========================
    @PutMapping("/{jobId}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> cancelJob(
            @PathVariable Long jobId,
            Authentication auth
    ) {

        jobService.cancelJob(jobId, auth);
        return ResponseEntity.ok().build();
    }

    // =========================
    // WORKER AVAILABILITY
    // =========================
    @GetMapping("/workers/availability")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<String> getAvailability(Authentication authentication) {

        return ResponseEntity.ok(
                jobService.getWorkerAvailability(authentication).name()
        );
    }

    @PutMapping("/workers/availability")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<String> updateAvailability(
            @RequestBody Map<String, String> body,
            Authentication authentication) {

        String status = body.get("status");
        jobService.updateWorkerAvailability(status, authentication);

        return ResponseEntity.ok("Availability updated");
    }

    // =========================
    // LOCATION FILTER
    // =========================
    @GetMapping("/by-location")
    public ResponseEntity<List<Job>> getJobsByLocation(
            @RequestParam String city,
            @RequestParam(required = false) String area) {

        return ResponseEntity.ok(
                jobService.getJobsByLocation(city, area)
        );
    }

}