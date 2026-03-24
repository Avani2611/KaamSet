package com.kaamSet.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
    name = "jobs",
    indexes = {
        @Index(name = "idx_job_status", columnList = "status"),
        @Index(name = "idx_job_city", columnList = "city"),
        @Index(name = "idx_job_user", columnList = "user_id"),
        @Index(name = "idx_job_worker", columnList = "worker_id")
    }
)
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // BASIC INFO
    // =========================

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    // =========================
    // LOCATION
    // =========================

    private String city;
    private String area;
    private String pincode;

    // =========================
    // JOB TYPE
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    // Only for CONTRACT jobs
    private Integer totalDays;

    // =========================
    // PRICING (SET BY WORKER)
    // =========================

    // For ONE_TIME jobs
    private Double oneTimePrice;

    // For CONTRACT jobs
    private Double perDaySalary;

    // =========================
    // STATUS
    // =========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    // =========================
    // RELATIONS
    // =========================

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private User worker;

    // =========================
    // CREATED TIME
    // =========================

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}