package com.bhaiya.dsatracker.services;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GmailEmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(GmailEmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final Random random = new Random();

    @Override
    public void sendSpacedRepetitionReminder(String toEmail, String userName, String patternName, String problemUrl) {
        
        int templateId = random.nextInt(6) + 1;
        String subject = "";
        String htmlBody = "";

        switch (templateId) {
            case 1:
                subject = "Bhai... 3 din ho gaye \uD83D\uDC40";
                htmlBody = String.format("<p>Kyu bhai? \uD83D\uDE05</p>" +
                        "<p>3 din ho gaye...</p>" +
                        "<p>DSA ne dump kar diya ya tracker ko? \uD83D\uDE02</p>" +
                        "<p>Chal, aaj bas 1 problem solve karte hain (Pattern: <strong>%s</strong>).</p>" +
                        "<p><a href=\"%s\" style=\"display:inline-block;padding:10px 20px;background-color:#10b981;color:#fff;text-decoration:none;border-radius:5px;margin-top:15px;\">Continue your streak \u2192</a></p>", patternName, problemUrl);
                break;
            case 2:
                subject = "Teri streak yaad kar rahi hai \uD83D\uDD25";
                htmlBody = String.format("<p>Oye %s,</p>" +
                        "<p>Teri streak thodi lonely feel kar rahi hai. \uD83E\uDD72</p>" +
                        "<p>Aaj sirf ek question solve kar de (Pattern: <strong>%s</strong>).</p>" +
                        "<p><a href=\"%s\" style=\"display:inline-block;padding:10px 20px;background-color:#10b981;color:#fff;text-decoration:none;border-radius:5px;margin-top:15px;\">Resume DSA \u2192</a></p>", userName, patternName, problemUrl);
                break;
            case 3:
                subject = "Babua wait kar raha hai \uD83D\uDE36";
                htmlBody = String.format("<p>Bhai %s,</p>" +
                        "<p>Babua Sheet wahi hai.</p>" +
                        "<p>Questions bhi wahi hain.</p>" +
                        "<p>Bas tu hi missing hai. \uD83D\uDE02</p>" +
                        "<p><a href=\"%s\" style=\"display:inline-block;padding:10px 20px;background-color:#10b981;color:#fff;text-decoration:none;border-radius:5px;margin-top:15px;\">Let's continue (%s) \u2192</a></p>", userName, problemUrl, patternName);
                break;
            case 4:
                subject = "Ek chhota sa reminder \u2764\uFE0F";
                htmlBody = String.format("<p>Bas yaad dilane aaya tha...</p>" +
                        "<p>DSA khud solve nahi hogi. \uD83D\uDE05</p>" +
                        "<p>Aaj 15 minute de de (<strong>%s</strong> pattern par).</p>" +
                        "<p>Future wala tu thank you bolega.</p>" +
                        "<p><a href=\"%s\" style=\"display:inline-block;padding:10px 20px;background-color:#10b981;color:#fff;text-decoration:none;border-radius:5px;margin-top:15px;\">Continue \u2192</a></p>", patternName, problemUrl);
                break;
            case 5:
                subject = "LeetCode khol ya guilt feel kar \uD83D\uDE02";
                htmlBody = String.format("<p>Bhai %s...</p>" +
                        "<p>Instagram kholne se pehle ek problem solve kar le.</p>" +
                        "<p>Bas ek (<strong>%s</strong>).</p>" +
                        "<p>Promise. \uD83E\uDD1D</p>" +
                        "<p><a href=\"%s\" style=\"display:inline-block;padding:10px 20px;background-color:#10b981;color:#fff;text-decoration:none;border-radius:5px;margin-top:15px;\">Let's Go \u2192</a></p>", userName, patternName, problemUrl);
                break;
            case 6:
                subject = "Bas thoda aur... \uD83D\uDE80";
                htmlBody = String.format("<p>Tu itna solve kar chuka hai...</p>" +
                        "<p>Ab rukna thoda unfair hai. \uD83D\uDE04</p>" +
                        "<p>Chalo aaj ka bhi tick laga dete hain (<strong>%s</strong> pattern par).</p>" +
                        "<p><a href=\"%s\" style=\"display:inline-block;padding:10px 20px;background-color:#10b981;color:#fff;text-decoration:none;border-radius:5px;margin-top:15px;\">Continue Solving \u2192</a></p>", patternName, problemUrl);
                break;
        }

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom("CTO Bhaiya <" + fromEmail + ">");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true indicates HTML content

            javaMailSender.send(message);
            logger.info("Gmail Email sent successfully to {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send Gmail to {}", toEmail, e);
        }
    }
}
