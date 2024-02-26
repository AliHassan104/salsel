package com.salsel.utils;

import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Awb;
import com.salsel.model.User;
import com.salsel.repository.AwbRepository;
import com.salsel.service.AwbService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class EmailUtils {
    private final JavaMailSender javaMailSender;
    private final AwbRepository awbRepository;
    private final AwbService awbService;


    public EmailUtils(JavaMailSender javaMailSender, AwbRepository awbRepository, AwbService awbService) {
        this.javaMailSender = javaMailSender;
        this.awbRepository = awbRepository;
        this.awbService = awbService;
    }

    @Value("${spring.mail.username}")
    private String sender;
    @Async
    public void sendEmail(String sender, String userEmail, Long awbId, byte[] pdfBytes) {

        Optional<Awb> optionalAwb = Optional.ofNullable(awbRepository.findByIdWhereStatusIsTrue(awbId));
        if (optionalAwb.isPresent()) {
            Awb awb = optionalAwb.get();

            try {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setFrom(sender);
                helper.setTo(userEmail);
                helper.setSubject("Welcome to Your Salsel!");

                String emailContent = "Dear Customer,\n\n"
                        + "Thank you for choosing Salsel Express! We are delighted to welcome you.\n\n"
                        + "Your Air Waybill (AWB) details:\n"
                        + "AWB Number: " + awb.getId() + "\n\n"
                        + "We appreciate your business and look forward to serving you.\n\n"
                        + "Best regards,\n"
                        + "Salsel Express Team";

                helper.setText(emailContent);

                // Attach the PDF
                helper.addAttachment("AWB_Details.pdf", new ByteArrayResource(pdfBytes));
//                javaMailSender.send(message);

            } catch (MessagingException e) {
                e.printStackTrace();
                System.err.println("Error sending email: " + e.getMessage());
            }

        } else {
            throw new RecordNotFoundException("Awb not found for id: " + awbId);
        }
    }

//    @Transactional
//    @Scheduled(fixedRate = 300000) // 5 minutes in milliseconds
//    public void sendScheduledEmail() {
//        List<Awb> awbList = awbRepository.findAllWhereStatusIsTrueAndEmailFlagIsFalse();
//
//        for (Awb awb : awbList) {
//            if (!awb.getEmailFlag()) {
//                byte[] pdf = awbService.downloadAwbPdf("awb_" + awb.getId() + ".pdf", awb.getId());
//
//                // Send email
////                sendEmail(sender, "muhammadtabish05@gmail.com", awb.getId(), pdf);
//
//                // Set the flag to true after sending the email
//                awb.setEmailFlag(true);
//                awbRepository.save(awb);
//            }
//        }
//    }

    @Async
    public void sendPasswordResetEmail(User user, String resetCode) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(user.getEmail());
            helper.setSubject("Password Reset Request");

            String resetLink = "http://localhost:8080/api/reset-password?email=" + user.getEmail() + "&code=" + resetCode;

            String emailContent = "Dear " + user.getName() + ",\n\n"
                    + "You have requested to reset your password. Please click on the following link to proceed with the password reset:\n\n"
                    + resetLink + "\n\n"
                    + "If you did not request a password reset, please ignore this email.\n\n"
                    + "Best regards,\n"
                    + "Salsel Team";

            helper.setText(emailContent);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    @Async
    public void sendWelcomeEmail(User user, String generatedPassword) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(user.getEmail());
            helper.setSubject("Welcome to Salassil Express");

            String emailContent = "Dear " + user.getName() + ",\n\n"
                    + "Welcome to Salassil Express! Your account has been successfully created.\n\n"
                    + "Your login details:\n"
                    + "Email: " + user.getEmail() + "\n"
                    + "Password: " + generatedPassword + "\n\n"
                    + "If you have any questions, feel free to contact our support team.\n\n"
                    + "Best regards,\n"
                    + "Salassil Express Team";

            helper.setText(emailContent);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    @Async
    public void sendPasswordRegeneratedEmail(User user, String newPassword) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(user.getEmail());
            helper.setSubject("Password Regenerated");

            String emailContent = "Dear " + user.getName() + ",\n\n"
                    + "Your password for Salassil Express has been successfully regenerated upon your request.\n\n"
                    + "Your new password is:\n"
                    + newPassword + "\n\n"
                    + "Please log in using this new password.\n\n"
                    + "If you did not request this password regeneration, please contact our support team immediately.\n\n"
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
