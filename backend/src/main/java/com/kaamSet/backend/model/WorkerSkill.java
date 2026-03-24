package com.kaamSet.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "worker_skills",
indexes = {
        @Index(name = "idx_worker_skill_worker", columnList = "worker_id"),
        @Index(name = "idx_worker_skill_skill", columnList = "skill_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"worker_id", "skill_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkerSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 👷 Worker
    @ManyToOne(optional = false)
    @JoinColumn(name = "worker_id")
    private User worker;

    // 🛠 Skill
    @ManyToOne(optional = false)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    // 📝 Description
    @Column(length = 500)
    private String description;

    // 📅 Experience
    @Column(name = "experience_years", nullable = false)
    private int experienceYears;

    // ✅ Verification (DEFAULT = false)
    @Builder.Default
    @Column(nullable = false)
    private boolean verified = false;
}
