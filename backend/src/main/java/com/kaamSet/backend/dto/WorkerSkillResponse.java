package com.kaamSet.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WorkerSkillResponse {

    private Long skillId;
    private String skillName;
    private int experienceYears;
    private boolean verified;
    private String skillCategory;
    private String verificationStatus; 
}
