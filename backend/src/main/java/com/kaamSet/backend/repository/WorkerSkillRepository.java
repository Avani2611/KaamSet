package com.kaamSet.backend.repository;

import com.kaamSet.backend.model.WorkerSkill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkerSkillRepository extends JpaRepository<WorkerSkill, Long> {

    boolean existsByWorker_IdAndSkill_Id(Long workerId, Long skillId);

    Page<WorkerSkill> findBySkill_Id(Long skillId, Pageable pageable);

    Page<WorkerSkill> findBySkill_IdAndVerifiedTrue(
            Long skillId,
            Pageable pageable
    );

    List<WorkerSkill> findByWorker_Id(Long workerId);
}
