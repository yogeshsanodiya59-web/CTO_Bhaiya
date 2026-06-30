package com.bhaiya.dsatracker;

import com.bhaiya.dsatracker.models.Pattern;
import com.bhaiya.dsatracker.models.Problem;
import com.bhaiya.dsatracker.repositories.PatternRepository;
import com.bhaiya.dsatracker.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (patternRepository.count() > 0) {
            System.out.println("Database already seeded with patterns. Skipping.");
            return;
        }

        System.out.println("Seeding database from all_quest file...");

        ClassPathResource resource = new ClassPathResource("all_quest");
        if (!resource.exists()) {
            System.out.println("Could not find all_quest file in resources. Skipping seeding.");
            return;
        }

        Map<String, Pattern> patternCache = new HashMap<>();
        int patternSortOrderCounter = 1;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("No.")) {
                    continue; // Skip headers or empty lines
                }

                String[] parts = line.split("\t");
                if (parts.length < 5) {
                    continue; // Incomplete line
                }

                String noStr = parts[0].trim();
                String patternName = parts[1].trim();
                // Sometimes pattern name gets enclosed in quotes due to Excel formatting
                if (patternName.startsWith("\"") && patternName.endsWith("\"")) {
                    patternName = patternName.substring(1, patternName.length() - 1);
                }
                patternName = patternName.replace("\n", "").replace("\r", "").trim();

                String rawTitle = parts[2].trim();
                if (rawTitle.startsWith("\"") && rawTitle.endsWith("\"")) {
                    rawTitle = rawTitle.substring(1, rawTitle.length() - 1);
                }

                String levelRaw = parts[3].trim().toLowerCase();
                String difficulty = null;
                if (levelRaw.contains("easy")) {
                    difficulty = "Easy";
                } else if (levelRaw.contains("medium")) {
                    difficulty = "Medium";
                } else if (levelRaw.contains("hard")) {
                    difficulty = "Hard";
                }
                
                String url = parts[4].trim();

                // Extract problem number if present (e.g. "88. Merge Sorted Array")
                Integer problemNumber = null;
                String problemTitle = rawTitle;
                java.util.regex.Pattern numPattern = java.util.regex.Pattern.compile("^(\\d+)\\.\\s*(.*)$");
                Matcher matcher = numPattern.matcher(rawTitle);
                if (matcher.matches()) {
                    problemNumber = Integer.parseInt(matcher.group(1));
                    problemTitle = matcher.group(2);
                }

                // Get or create Pattern
                Pattern pattern = patternCache.get(patternName);
                if (pattern == null) {
                    pattern = new Pattern();
                    pattern.setName(patternName);
                    pattern.setSortOrder(patternSortOrderCounter++);
                    pattern = patternRepository.save(pattern);
                    patternCache.put(patternName, pattern);
                }

                // Parse sort order
                int sortOrder = 0;
                try {
                    sortOrder = Integer.parseInt(noStr);
                } catch (NumberFormatException ignored) {}

                Problem problem = new Problem();
                problem.setPattern(pattern);
                problem.setNumber(problemNumber);
                problem.setTitle(problemTitle);
                problem.setDifficulty(difficulty);
                problem.setLeetcodeUrl(url);
                problem.setSortOrder(sortOrder);

                problemRepository.save(problem);
            }
            System.out.println("Finished seeding database! Added " + problemRepository.count() + " problems across " + patternCache.size() + " patterns.");
        }
    }
}
