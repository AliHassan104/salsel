package com.salsel.utils;

import com.salsel.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class AssignmentEmailsUtils {
    private final JavaMailSender javaMailSender;

    public AssignmentEmailsUtils(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendAssignedAwb(User user, Long awbNumber,Long id) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(user.getEmail());
            helper.setSubject("Airbill Assignment - AWB Number " + awbNumber);

            String resetLink = "http://localhost:4200/#/awb/list/"+id;
            String emailContent = "Dear " + user.getName() + ",\n\n"
                    + "An airbill with ID " + awbNumber +  " has been assigned to you.\n"
                    + "Kindly review it urgently and take necessary action.\n\n"
                    + resetLink + "\n\n\n"
                    + "Best regards,\n"
                    + "Salassil Express Team";

            helper.setText(emailContent);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}
