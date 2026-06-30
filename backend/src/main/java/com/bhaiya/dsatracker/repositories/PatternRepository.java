package com.bhaiya.dsatracker.repositories;

import com.bhaiya.dsatracker.models.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatternRepository extends JpaRepository<Pattern, Long> {
    List<Pattern> findAllByOrderBySortOrderAsc();
}
