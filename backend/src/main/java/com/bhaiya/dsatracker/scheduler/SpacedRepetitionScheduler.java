package com.bhaiya.dsatracker.scheduler;

import com.bhaiya.dsatracker.models.UserProgress;
import com.bhaiya.dsatracker.repositories.UserProgressRepository;
import com.bhaiya.dsatracker.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class SpacedRepetitionScheduler {

    private static final Logger logger = LoggerFactory.getLogger(SpacedRepetitionScheduler.class);
    
    private final UserProgressRepository userProgressRepository;
    private final EmailService emailService;

    @Autowired
    public SpacedRepetitionScheduler(UserProgressRepository userProgressRepository, EmailService emailService) {
        this.userProgressRepository = userProgressRepository;
        this.emailService = emailService;
    }

    // Runs every day at 10:00 AM IST.
    @Scheduled(cron = "0 0 10 * * ?", zone = "Asia/Kolkata")
    public void sendRevisionEmails() {
        logger.info("Running daily Spaced Repetition job...");
        
        // Check for 3, 7, and 30 days ago
        int[] intervals = {3, 7, 30};
        
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        for (int daysAgo : intervals) {
            // Get the start and end of the day exactly 'daysAgo' days ago
            OffsetDateTime targetDate = now.minusDays(daysAgo);
            OffsetDateTime startOfDay = targetDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
            OffsetDateTime endOfDay = targetDate.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            
            List<UserProgress> records = userProgressRepository.findByCompletedTrueAndCompletedAtBetween(startOfDay, endOfDay);
            
            logger.info("Found {} records completed exactly {} days ago.", records.size(), daysAgo);
            
            for (UserProgress record : records) {
                if (record.getUser() != null && record.getUser().getEmail() != null && record.getProblem() != null) {
                    String patternName = record.getProblem().getPattern() != null ? record.getProblem().getPattern().getName() : "a pattern";
                    String problemUrl = record.getProblem().getLeetcodeUrl() != null ? record.getProblem().getLeetcodeUrl() : "https://leetcode.com";
                    
                    emailService.sendSpacedRepetitionReminder(
                        record.getUser().getEmail(),
                        record.getUser().getName() != null ? record.getUser().getName() : "Student",
                        patternName,
                        problemUrl
                    );
                }
            }
        }
        
        logger.info("Finished daily Spaced Repetition job.");
    }
}
