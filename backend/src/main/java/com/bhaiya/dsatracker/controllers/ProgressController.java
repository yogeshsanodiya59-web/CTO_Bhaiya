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

    @GetMapping("/bookmarks")
    public Map<Long, Boolean> getUserBookmarks(@AuthenticationPrincipal String email) {
        User user = getOrCreateUser(email);
        List<UserProgress> progressList = userProgressRepository.findByUserId(user.getId());
        return progressList.stream()
                .collect(Collectors.toMap(
                        p -> p.getProblem().getId(),
                        UserProgress::getBookmarked
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

        if (completed) {
            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.LocalDate lastActive = user.getLastActiveDate();
            if (lastActive == null || lastActive.isBefore(today.minusDays(1))) {
                user.setCurrentStreak(1);
            } else if (lastActive.equals(today.minusDays(1))) {
                user.setCurrentStreak((user.getCurrentStreak() != null ? user.getCurrentStreak() : 0) + 1);
            }
            // if lastActive == today, streak remains same
            user.setLastActiveDate(today);
            userRepository.save(user);
        }

        return ResponseEntity.ok(Map.of("success", true, "completed", completed, "streak", user.getCurrentStreak()));
    }

    @PostMapping("/{problemId}/bookmark")
    public ResponseEntity<?> toggleBookmark(@PathVariable Long problemId, @RequestBody Map<String, Boolean> body, @AuthenticationPrincipal String email) {
        User user = getOrCreateUser(email);
        Boolean bookmarked = body.getOrDefault("bookmarked", true);

        Optional<Problem> problemOpt = problemRepository.findById(problemId);
        if (problemOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserProgress progress = userProgressRepository.findByUserIdAndProblemId(user.getId(), problemId)
                .orElse(new UserProgress());

        progress.setUser(user);
        progress.setProblem(problemOpt.get());
        progress.setBookmarked(bookmarked);

        userProgressRepository.save(progress);

        return ResponseEntity.ok(Map.of("success", true, "bookmarked", bookmarked));
    }
}
