package com.salsel.utils;

import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Awb;
import com.salsel.repository.AwbRepository;
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

    public EmailUtils(JavaMailSender javaMailSender, AwbRepository awbRepository) {
        this.javaMailSender = javaMailSender;
        this.awbRepository = awbRepository;
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
                javaMailSender.send(message);

            } catch (MessagingException e) {
                e.printStackTrace();
                System.err.println("Error sending email: " + e.getMessage());
            }

        } else {
            throw new RecordNotFoundException("Awb not found for id: " + awbId);
        }
    }

    @Transactional
    @Scheduled(fixedRate = 900000) // 15 minutes in milliseconds
    public void sendScheduledEmail() {
        List<Awb> awbList = awbRepository.findAllWhereStatusIsTrueAndEmailFlagIsFalse();

        for (Awb awb : awbList) {
            if (!awb.getEmailFlag()) {
                // Send email
//                sendEmail(sender, "muhammadtabish05@gmail.com", awb.getId(), USER);

                // Set the flag to true after sending the email
                awb.setEmailFlag(true);
                awbRepository.save(awb);
            }
        }
    }

}
