package com.bhaiya.dsatracker.controllers;

import com.bhaiya.dsatracker.models.Pattern;
import com.bhaiya.dsatracker.models.Problem;
import com.bhaiya.dsatracker.repositories.PatternRepository;
import com.bhaiya.dsatracker.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sheet")
@CrossOrigin(origins = "*") // In production, restrict this to frontend URL
public class SheetController {

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @GetMapping("/patterns")
    public List<Pattern> getPatterns() {
        return patternRepository.findAllByOrderBySortOrderAsc();
    }

    @GetMapping("/problems")
    public List<Problem> getProblems() {
        return problemRepository.findAllByOrderBySortOrderAsc();
    }
}
