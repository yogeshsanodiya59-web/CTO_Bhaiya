package com.bhaiya.dsatracker.services;

public interface EmailService {
    void sendSpacedRepetitionReminder(String toEmail, String userName, String patternName, String problemUrl);
}
