package com.kaamSet.backend.service;

import com.kaamSet.backend.dto.JobRequest;
import com.kaamSet.backend.dto.JobResponse;
import com.kaamSet.backend.dto.UserResponse;
import com.kaamSet.backend.model.*;
import com.kaamSet.backend.repository.JobRepository;
import com.kaamSet.backend.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    // =========================================================
    // CREATE JOB
    // =========================================================
    @Transactional
    public Job createJob(JobRequest request) {

        User user = getCurrentUser();

        validateUserRole(user, Role.USER, "Only customers can create jobs");

        Job job = buildJobFromRequest(request, user);

        return jobRepository.save(job);
    }

    // =========================================================
    // AVAILABLE JOBS
    // =========================================================
    public Page<JobResponse> getAvailableJobsForWorker(Pageable pageable) {
        return jobRepository.findByStatus(JobStatus.PENDING, pageable)
                .map(this::mapToResponse);
    }

    // =========================================================
    // ACCEPT JOB (WORKER SETS PRICE)
    // =========================================================
        @Transactional
        public void acceptJob(
                Long jobId,
                Double oneTimePrice,
                Double perDaySalary,
                Authentication authentication
        ) {

            Job job = getJobOrThrow(jobId);
            validateJobStatus(job, JobStatus.PENDING, "Job already accepted or closed");

            User worker = getCurrentUser(authentication);
            validateUserRole(worker, Role.WORKER, "Only workers can accept jobs");

            if (worker.getAvailability() != WorkerAvailability.AVAILABLE) {
                throw new IllegalStateException("You must be AVAILABLE to accept jobs");
            }

            if (job.getUser().getId().equals(worker.getId())) {
                throw new IllegalStateException("You cannot accept your own job");
            }

    // =========================
    // PRICE VALIDATION LOGIC
    // =========================

        if (job.getJobType() == JobType.ONE_TIME) {

            if (oneTimePrice == null || oneTimePrice <= 0) {
                throw new IllegalArgumentException("One-time price must be provided and positive");
            }

            job.setOneTimePrice(oneTimePrice);
            job.setPerDaySalary(null);

        } else if (job.getJobType() == JobType.CONTRACT) {

            if (perDaySalary == null || perDaySalary <= 0) {
                throw new IllegalArgumentException("Per day salary must be provided and positive");
            }

            job.setPerDaySalary(perDaySalary);
            job.setOneTimePrice(null);
        }

        job.setWorker(worker);
        job.setStatus(JobStatus.ACCEPTED);
        worker.setAvailability(WorkerAvailability.BUSY);
    }


    // =========================================================
    // COMPLETE JOB
    // =========================================================
    @Transactional
    public void completeJob(Long jobId, Authentication authentication) {

        Job job = getJobOrThrow(jobId);
        validateJobStatus(job, JobStatus.ACCEPTED, "Job is not in ACCEPTED state");

        User worker = getCurrentUser(authentication);
        validateUserRole(worker, Role.WORKER, "Only workers can complete jobs");

        if (job.getWorker() == null ||
                !job.getWorker().getId().equals(worker.getId())) {
            throw new IllegalStateException("Access denied");
        }

        job.setStatus(JobStatus.COMPLETED);
        worker.setAvailability(WorkerAvailability.AVAILABLE);
    }

    // =========================================================
    // USER HISTORY
    // =========================================================
    public Page<JobResponse> getUserJobHistory(Pageable pageable) {
        User user = getCurrentUser();
        return jobRepository.findByUser_Id(user.getId(), pageable)
                .map(this::mapToResponse);
    }


    // =========================================================
    // WORKER ACTIVE JOBS
    // =========================================================
    public Page<JobResponse> getWorkerActiveJobs(Pageable pageable) {

        User worker = getCurrentUser();

        return jobRepository
                .findByWorker_IdAndStatus(worker.getId(), JobStatus.ACCEPTED, pageable)
                .map(this::mapToResponse);
    }

    // =========================================================
    // WORKER HISTORY
    // =========================================================
    public Page<JobResponse> getWorkerJobHistory(Pageable pageable) {
        User worker = getCurrentUser();
        return jobRepository.findByWorker_Id(worker.getId(), pageable)
                .map(this::mapToResponse);
    }

    // =========================================================
    // CANCEL JOB
    // =========================================================
    @Transactional
    public void cancelJob(Long jobId, Authentication authentication) {

        Job job = getJobOrThrow(jobId);
        User customer = getCurrentUser(authentication);

        if (!job.getUser().getId().equals(customer.getId())) {
            throw new IllegalStateException("You can cancel only your own jobs");
        }

        validateJobStatus(job, JobStatus.PENDING,
                "Job cannot be cancelled after acceptance");

        job.setStatus(JobStatus.CANCELLED);
    }

    // =========================================================
    // WORKER AVAILABILITY
    // =========================================================
    public WorkerAvailability getWorkerAvailability(Authentication authentication) {
        return getCurrentUser(authentication).getAvailability();
    }

    @Transactional
    public void updateWorkerAvailability(String status, Authentication authentication) {

        User worker = getCurrentUser(authentication);

        try {
            WorkerAvailability availability =
                    WorkerAvailability.valueOf(status.toUpperCase());

            worker.setAvailability(availability);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid availability status");
        }
    }

    // =========================================================
    // LOCATION FILTER
    // =========================================================
    public List<Job> getJobsByLocation(String city, String area) {
        return (area != null && !area.isBlank())
                ? jobRepository.findByCityAndArea(city, area)
                : jobRepository.findByCity(city);
    }

    // =========================================================
    // MAPPING
        // =========================================================
    public JobResponse mapToResponse(Job job) {

        UserResponse customer = toUserResponse(job.getUser());
        UserResponse worker = job.getWorker() != null
                ? toUserResponse(job.getWorker())
                : null;

        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getCity(),
                job.getArea(),
                job.getPincode(),
                job.getJobType(),
                job.getTotalDays(),
                job.getOneTimePrice(),
                job.getPerDaySalary(),
                job.getStatus(),
                customer,
                worker,
                job.getCreatedAt()
        );
    }

    // =========================================================
    // PRIVATE HELPERS
    // =========================================================

    private User getCurrentUser() {
        return getCurrentUser(
                SecurityContextHolder.getContext().getAuthentication()
        );
    }

    private User getCurrentUser(Authentication authentication) {
        return userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private Job getJobOrThrow(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));
    }

    private void validateUserRole(User user, Role role, String message) {
        if (user.getRole() != role) {
            throw new IllegalStateException(message);
        }
    }

    private void validateJobStatus(Job job, JobStatus status, String message) {
        if (job.getStatus() != status) {
            throw new IllegalStateException(message);
        }
    }

private Job buildJobFromRequest(JobRequest request, User user) {

    Job job = new Job();
    job.setTitle(request.getTitle());
    job.setDescription(request.getDescription());
    job.setCity(request.getCity());
    job.setArea(request.getArea());
    job.setPincode(request.getPincode());
    job.setStatus(JobStatus.PENDING);
    job.setUser(user);
    job.setJobType(request.getJobType());

    // ✅ If CONTRACT → totalDays is required
    if (request.getJobType() == JobType.CONTRACT) {

        if (request.getTotalDays() == null || request.getTotalDays() < 1) {
            throw new IllegalArgumentException("Total days is required for contract jobs");
        }

        job.setTotalDays(request.getTotalDays());

    } else {
        // ✅ If ONE_TIME → no days needed
        job.setTotalDays(null);
    }

    return job;
}

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole()
        );
    }
}