package com.kaamSet.backend.repository;

import com.kaamSet.backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Prevent duplicate reviews
    Optional<Review> findByJob_IdAndUser_Id(Long jobId, Long userId);

    // Worker reviews (list)
    List<Review> findByJob_Worker_Id(Long workerId);

    // Worker reviews (paginated)
    Page<Review> findByJob_Worker_Id(Long workerId, Pageable pageable);

@Query("""
    SELECT 
        COALESCE(AVG(r.rating), 0),
        COUNT(r.id)
    FROM Review r
    WHERE r.job.worker.id = :workerId
""")
List<Object[]> getWorkerRatingStats(Long workerId);


@Query("""
    SELECT 
        r.job.worker.id,
        COALESCE(AVG(r.rating), 0),
        COUNT(r.id)
    FROM Review r
    WHERE r.job.worker.id IN :workerIds
    GROUP BY r.job.worker.id
""")
List<Object[]> getWorkerRatingStatsBulk(
        @Param("workerIds") List<Long> workerIds
);

boolean existsByJob_IdAndUser_Id(Long jobId, Long userId);

}

