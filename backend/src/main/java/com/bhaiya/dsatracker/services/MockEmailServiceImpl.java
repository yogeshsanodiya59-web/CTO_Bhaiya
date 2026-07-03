package com.bhaiya.dsatracker.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MockEmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(MockEmailServiceImpl.class);

    @Override
    public void sendSpacedRepetitionReminder(String toEmail, String userName, String patternName, String problemUrl) {
        String emailBody = String.format(
            "\n======================================================\n" +
            "MOCK EMAIL SENT TO: %s\n" +
            "SUBJECT: Time to revise %s! \uD83D\uDE80\n" + // rocket emoji
            "------------------------------------------------------\n" +
            "Hey %s! \uD83D\uDC4B\n\n" + // waving hand emoji
            "It's been a while since you conquered the **%s** pattern.\n" +
            "To make sure you don't forget it in your interview, here is 1 quick revision question:\n" +
            "%s\n\n" +
            "- CTO Bhaiya\n" +
            "======================================================",
            toEmail, patternName, userName, patternName, problemUrl
        );

        logger.info(emailBody);
    }
}
