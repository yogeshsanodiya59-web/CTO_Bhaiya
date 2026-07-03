package com.bhaiya.dsatracker.models;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "problem_id"})
})
public class UserProgress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    private Boolean completed = false;

    private Boolean bookmarked = false;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Problem getProblem() { return problem; }
    public void setProblem(Problem problem) { this.problem = problem; }
    public Boolean getCompleted() { return completed != null ? completed : false; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
    public Boolean getBookmarked() { return bookmarked != null ? bookmarked : false; }
    public void setBookmarked(Boolean bookmarked) { this.bookmarked = bookmarked; }
    public OffsetDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(OffsetDateTime completedAt) { this.completedAt = completedAt; }
}
