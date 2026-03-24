package com.kaamSet.backend.model;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "users",
indexes = {
        @Index(name = "idx_user_email", columnList = "email")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder.Default
    private boolean active = true;

    @OneToMany(mappedBy = "user")
    private List<Job> createdJobs;

    // ✅ NEW LOCATION FIELDS
    @Column(nullable = true)
    private String city;

    @Column(nullable = true)
    private String area;

    @Column(nullable = true)
    private String pincode;

    @Column(nullable = true)
    private Double latitude;

    @Column(nullable = true)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private WorkerAvailability availability = WorkerAvailability.OFFLINE;

    public String testGetter() {
    return this.email;
}
}
