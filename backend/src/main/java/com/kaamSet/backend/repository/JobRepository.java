package com.kaamSet.backend.repository;

import java.util.List;
import com.kaamSet.backend.model.Job;
import com.kaamSet.backend.model.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByStatus(JobStatus status, Pageable pageable);

    Page<Job> findByUser_Id(Long userId, Pageable pageable);

    Page<Job> findByWorker_Id(Long workerId, Pageable pageable);

    Page<Job> findByCityIgnoreCase(String city, Pageable pageable);

    Page<Job> findByCityIgnoreCaseAndAreaIgnoreCase(
            String city,
            String area,
            Pageable pageable
    );

    Page<Job> findByStatusAndCityIgnoreCase(
            JobStatus status,
            String city,
            Pageable pageable
    );

    List<Job> findByCity(String city);

    List<Job> findByCityAndArea(String city, String area);

    Page<Job> findByWorker_IdAndStatus(Long workerId, JobStatus status, Pageable pageable);
}