package com.bhaiya.dsatracker.repositories;

import com.bhaiya.dsatracker.models.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findAllByOrderBySortOrderAsc();
    List<Problem> findByPatternIdOrderBySortOrderAsc(Long patternId);
}
