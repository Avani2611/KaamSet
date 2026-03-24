package com.kaamSet.backend.repository;

import com.kaamSet.backend.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByNameContainingIgnoreCase(String keyword);
}
