package com.salsel.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailUtils {
    private final JavaMailSender javaMailSender;

    public EmailUtils(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String sender;
    @Async
    public void sendEmail(String sender, String userEmail, String awbNumber, byte[] pdfBytes) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(sender);
            helper.setTo(userEmail);
            helper.setSubject("Welcome to Your Salsel!");

            String emailContent = "Dear Customer,\n\n"
                    + "Thank you for choosing Salsel Express! We are delighted to welcome you.\n\n"
                    + "Your Air Waybill (AWB) details:\n"
                    + "AWB Number: " + awbNumber + "\n\n"
                    + "We appreciate your business and look forward to serving you.\n\n"
                    + "Best regards,\n"
                    + "Salsel Express Team";

            helper.setText(emailContent);

            // Attach the PDF
            helper.addAttachment("AWB_Details.pdf", new ByteArrayResource(pdfBytes));

            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error sending email: " + e.getMessage());
        }
    }


}
