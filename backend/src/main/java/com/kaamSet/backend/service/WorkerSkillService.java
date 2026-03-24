package com.kaamSet.backend.service;

import com.kaamSet.backend.model.Role;
import com.kaamSet.backend.model.Skill;
import com.kaamSet.backend.model.User;
import com.kaamSet.backend.model.WorkerSkill;
import com.kaamSet.backend.repository.SkillRepository;
import com.kaamSet.backend.repository.UserRepository;
import com.kaamSet.backend.repository.WorkerSkillRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class WorkerSkillService {

    private final WorkerSkillRepository workerSkillRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public WorkerSkillService(
            WorkerSkillRepository workerSkillRepository,
            UserRepository userRepository,
            SkillRepository skillRepository) {

        this.workerSkillRepository = workerSkillRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    public void addSkillToWorker(
            Long skillId,
            int experienceYears,
            String description,
            Authentication authentication) {

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Unauthorized access");
        }

        String email = authentication.getName();

        // ✅ Case-insensitive email search
        User worker = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Role check
        if (worker.getRole() != Role.WORKER) {
            throw new RuntimeException("Access denied. Only workers can add skills.");
        }

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        boolean alreadyExists =
                workerSkillRepository.existsByWorker_IdAndSkill_Id(
                        worker.getId(), skillId);

        if (alreadyExists) {
            throw new RuntimeException("Skill already added");
        }

        WorkerSkill workerSkill = WorkerSkill.builder()
                .worker(worker)
                .skill(skill)
                .experienceYears(experienceYears)
                .description(description)
                .verified(false)
                .build();

        workerSkillRepository.save(workerSkill);
    }
}