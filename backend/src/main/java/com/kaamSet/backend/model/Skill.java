package com.kaamSet.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "skills", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
},
indexes = {
@Index(name = "idx_skill_name", columnList = "name")
}
)


public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    
}
