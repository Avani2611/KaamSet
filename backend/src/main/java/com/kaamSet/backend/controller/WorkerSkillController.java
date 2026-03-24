package com.kaamSet.backend.controller;

import com.kaamSet.backend.dto.WorkerSkillRequest;
import com.kaamSet.backend.service.WorkerSkillService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/worker-skills")
public class WorkerSkillController {

    private final WorkerSkillService workerSkillService;

    public WorkerSkillController(WorkerSkillService workerSkillService) {
        this.workerSkillService = workerSkillService;
    }

    @PostMapping
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<String> addSkill(
            @Valid @RequestBody WorkerSkillRequest request,
            Authentication authentication) {

        workerSkillService.addSkillToWorker(
                request.getSkillId(),
                request.getExperienceYears(),
                request.getDescription(),
                authentication
        );

        return ResponseEntity.ok("Skill added successfully");
    }
}