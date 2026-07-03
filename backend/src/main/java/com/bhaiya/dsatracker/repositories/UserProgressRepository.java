package com.bhaiya.dsatracker.repositories;

import com.bhaiya.dsatracker.models.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findByUserId(UUID userId);
    Optional<UserProgress> findByUserIdAndProblemId(UUID userId, Long problemId);
    List<UserProgress> findByCompletedTrueAndCompletedAtBetween(java.time.OffsetDateTime start, java.time.OffsetDateTime end);
}
