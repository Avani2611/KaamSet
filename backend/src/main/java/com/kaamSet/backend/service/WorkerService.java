package com.kaamSet.backend.service;

import com.kaamSet.backend.dto.*;
import com.kaamSet.backend.model.*;
import com.kaamSet.backend.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkerService {

    private final WorkerSkillRepository workerSkillRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public WorkerService(
            UserRepository userRepository,
            ReviewRepository reviewRepository,
            WorkerSkillRepository workerSkillRepository
    ) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.workerSkillRepository = workerSkillRepository;
    }

    // ===================== WORKER PROFILE =====================

public WorkerProfileResponse getWorkerProfile(Long workerId) {

    User worker = userRepository.findById(workerId)
            .orElseThrow(() -> new RuntimeException("Worker not found"));

    // 🔥 Reuse existing rating method (no 100 review limit)
    WorkerRatingResponse rating = getWorkerRating(workerId);

    return WorkerProfileResponse.builder()
            .workerId(worker.getId())
            .fullName(worker.getFullName())
            .email(worker.getEmail())
            .averageRating(rating.getAverageRating())
            .totalReviews(rating.getTotalReviews())
            .build();
}

    // ===================== WORKER REVIEWS =====================

    public Page<ReviewResponse> getWorkerReviews(Long workerId, Pageable pageable) {
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

    // ===================== WORKER RATING =====================

public WorkerRatingResponse getWorkerRating(Long workerId) {

    List<Object[]> result = reviewRepository.getWorkerRatingStats(workerId);

    if (result.isEmpty()) {
        return new WorkerRatingResponse(0.0, 0L);
    }

    Object[] row = result.get(0);

    double avg = ((Number) row[0]).doubleValue();
    long totalReviews = ((Number) row[1]).longValue();

    // ✅ round to 1 decimal
    avg = Math.round(avg * 10.0) / 10.0;

    return new WorkerRatingResponse(avg, totalReviews);
}


    // ===================== SEARCH WORKERS =====================

    public Page<WorkerSearchResponse> getWorkersBySkill(
            Long skillId,
            int page,
            int size,
            String sort,
            Double userLat,
            Double userLng
    ) {

        if (skillId == null || skillId <= 0) {
            throw new IllegalArgumentException("Invalid skillId");
        }

        // ✅ Normalize sort ONCE
        final String sortBy;
        if ("reviews".equalsIgnoreCase(sort)) {
            sortBy = "reviews";
        } else if ("distance".equalsIgnoreCase(sort)) {
            sortBy = "distance";
        } else {
            sortBy = "rating";
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<WorkerSkill> workerSkillPage =
                workerSkillRepository.findBySkill_IdAndVerifiedTrue(skillId, pageable);

        if (workerSkillPage.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> workerIds = workerSkillPage.stream()
                .map(ws -> ws.getWorker().getId())
                .toList();

        Map<Long, WorkerRatingResponse> ratingMap =
                reviewRepository.getWorkerRatingStatsBulk(workerIds)
                        .stream()
                        .collect(Collectors.toMap(
                                row -> (Long) row[0],
                                row -> new WorkerRatingResponse(
                                        ((Number) row[1]).doubleValue(),
                                        ((Number) row[2]).longValue()
                                )
                        ));

        // ✅ MUTABLE LIST (CRITICAL FIX)
        List<WorkerSearchResponse> results = new ArrayList<>();

        for (WorkerSkill ws : workerSkillPage.getContent()) {

            User worker = ws.getWorker();

            WorkerRatingResponse rating =
                    ratingMap.getOrDefault(worker.getId(),
                            new WorkerRatingResponse(0.0, 0L));

            Double distanceKm = null;

            if ("distance".equals(sortBy)
                    && userLat != null && userLng != null
                    && worker.getLatitude() != null
                    && worker.getLongitude() != null) {

                distanceKm = calculateDistance(
                        userLat,
                        userLng,
                        worker.getLatitude(),
                        worker.getLongitude()
                );
            }

            results.add(
                    WorkerSearchResponse.builder()
                            .workerId(worker.getId())
                            .fullName(worker.getFullName())
                            .email(worker.getEmail())
                            .averageRating(rating.getAverageRating())
                            .totalReviews(rating.getTotalReviews())
                            .distanceKm(distanceKm)
                            .build()
            );
        }

        // ===================== SORT =====================

        switch (sortBy) {
            case "distance" ->
                    results.sort(
                            Comparator.comparing(
                                    w -> Optional.ofNullable(w.getDistanceKm())
                                            .orElse(Double.MAX_VALUE)
                            )
                    );

            case "reviews" ->
                    results.sort(
                            Comparator.comparingLong(WorkerSearchResponse::getTotalReviews)
                                    .reversed()
                    );

            default ->
                    results.sort(
                            Comparator.comparingDouble(WorkerSearchResponse::getAverageRating)
                                    .reversed()
                    );
        }

        return new PageImpl<>(results, pageable, workerSkillPage.getTotalElements());
    }

    // ===================== DISTANCE UTILITY =====================

    private double calculateDistance(
            double lat1, double lon1,
            double lat2, double lon2
    ) {
        final int R = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        return R * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }
}
