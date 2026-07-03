package com.bhaiya.dsatracker.repositories;

import com.bhaiya.dsatracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    @org.springframework.data.jpa.repository.Query(
        "SELECT u.name as name, u.email as email, COUNT(up.id) as questionsCompleted " +
        "FROM User u JOIN UserProgress up ON u.id = up.user.id " +
        "WHERE up.completed = true " +
        "GROUP BY u.id, u.name, u.email " +
        "ORDER BY questionsCompleted DESC"
    )
    java.util.List<com.bhaiya.dsatracker.models.LeaderboardEntry> getLeaderboard();
}
