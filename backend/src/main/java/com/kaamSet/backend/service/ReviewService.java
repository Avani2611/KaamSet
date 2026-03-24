package com.kaamSet.backend.service;

import com.kaamSet.backend.dto.ReviewRequest;
import com.kaamSet.backend.dto.ReviewResponse;
import com.kaamSet.backend.model.*;
import com.kaamSet.backend.repository.JobRepository;
import com.kaamSet.backend.repository.ReviewRepository;
import com.kaamSet.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         JobRepository jobRepository,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    // ✅ ADD REVIEW
public void addReview(Long jobId, ReviewRequest request, Authentication auth) {

    // 1️⃣ Job must exist
    Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("Job not found"));

    // 2️⃣ Job must be COMPLETED
    if (!job.getStatus().equals(JobStatus.COMPLETED)) {
        throw new RuntimeException("You can review only after job completion");
    }

    // 3️⃣ Logged-in user must exist
    User customer = userRepository.findByEmailIgnoreCase(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));

    // 4️⃣ Logged-in user must be job owner
    if (!job.getUser().getId().equals(customer.getId())) {
        throw new RuntimeException("You are not allowed to review this job");
    }

    // 5️⃣ Worker must exist
    if (job.getWorker() == null) {
        throw new RuntimeException("Job has no assigned worker");
    }

    // 6️⃣ Prevent duplicate reviews
    reviewRepository.findByJob_IdAndUser_Id(jobId, customer.getId())
            .ifPresent(r -> {
                throw new RuntimeException("Review already exists for this job");
            });

    // 7️⃣ Save review
    Review review = Review.builder()
            .rating(request.getRating())
            .comment(request.getComment())
            .job(job)
            .user(customer)
            .build();

    reviewRepository.save(review);
}





    // ✅ PAGINATED WORKER REVIEWS
    public Page<ReviewResponse> getReviewsForWorker(Long workerId, Pageable pageable) {
        return reviewRepository
                .findByJob_Worker_Id(workerId, pageable)
                .map(this::mapToResponse);
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .userName(review.getUser().getFullName())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
