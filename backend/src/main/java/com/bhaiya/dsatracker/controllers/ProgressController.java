package com.bhaiya.dsatracker.controllers;

import com.bhaiya.dsatracker.models.Problem;
import com.bhaiya.dsatracker.models.User;
import com.bhaiya.dsatracker.models.UserProgress;
import com.bhaiya.dsatracker.repositories.ProblemRepository;
import com.bhaiya.dsatracker.repositories.UserProgressRepository;
import com.bhaiya.dsatracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "*")
public class ProgressController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private ProblemRepository problemRepository;

    private User getOrCreateUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping
    public Map<Long, Boolean> getUserProgress(@AuthenticationPrincipal String email) {
        User user = getOrCreateUser(email);
        List<UserProgress> progressList = userProgressRepository.findByUserId(user.getId());
        return progressList.stream()
                .collect(Collectors.toMap(
                        p -> p.getProblem().getId(),
                        UserProgress::getCompleted
                ));
    }

    @PostMapping("/{problemId}")
    public ResponseEntity<?> toggleProgress(@PathVariable Long problemId, @RequestBody Map<String, Boolean> body, @AuthenticationPrincipal String email) {
        User user = getOrCreateUser(email);
        Boolean completed = body.getOrDefault("completed", true);

        Optional<Problem> problemOpt = problemRepository.findById(problemId);
        if (problemOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserProgress progress = userProgressRepository.findByUserIdAndProblemId(user.getId(), problemId)
                .orElse(new UserProgress());

        progress.setUser(user);
        progress.setProblem(problemOpt.get());
        progress.setCompleted(completed);
        progress.setCompletedAt(completed ? OffsetDateTime.now() : null);

        userProgressRepository.save(progress);

        return ResponseEntity.ok(Map.of("success", true, "completed", completed));
    }
}
