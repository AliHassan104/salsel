package com.salsel.utils;

import com.salsel.dto.BillingDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Account;
import com.salsel.model.Awb;
import com.salsel.model.Ticket;
import com.salsel.model.User;
import com.salsel.repository.AccountRepository;
import com.salsel.repository.AwbRepository;
import com.salsel.service.ExcelGenerationService;
import com.salsel.service.PdfGenerationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.salsel.constants.AwbStatusConstants.DELIVERED;
import static com.salsel.constants.AwbStatusConstants.PICKED_UP;
import static com.salsel.constants.BillingConstants.ACCOUNT_NUMBER;

@Component
public class EmailUtils {
    private final JavaMailSender javaMailSender;
    private final AwbRepository awbRepository;
    private final ExcelGenerationService excelGenerationService;
    private final AccountRepository accountRepository;
    private final PdfGenerationService pdfGenerationService;


    public EmailUtils(JavaMailSender javaMailSender, AwbRepository awbRepository, ExcelGenerationService excelGenerationService, AccountRepository accountRepository, PdfGenerationService pdfGenerationService) {
        this.javaMailSender = javaMailSender;
        this.awbRepository = awbRepository;
        this.excelGenerationService = excelGenerationService;
        this.accountRepository = accountRepository;
        this.pdfGenerationService = pdfGenerationService;
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

    @Async
    public void sendAlertEmailForPendingTickets(List<Ticket> tickets, String toAddress, String[] ccAddresses) throws MessagingException {
        StringBuilder emailBody = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        emailBody.append("<html><body>");
        emailBody.append("<h2 style='color: black;'>Pending Tickets Summary</h2>");
        emailBody.append("<table style='border-collapse: collapse; border: 1px solid black; color: black;'>");
        emailBody.append("<tr><th style='border: 1px solid black; font-weight: bold; color: black;'>Ticket ID</th>");
        emailBody.append("<th style='border: 1px solid black; font-weight: bold; color: black;'>Created At</th>");
        emailBody.append("<th style='border: 1px solid black; font-weight: bold; color: black;'>Shipper</th>");
        emailBody.append("<th style='border: 1px solid black; font-weight: bold; color: black;'>Recipient</th>");
        emailBody.append("<th style='border: 1px solid black; font-weight: bold; color: black;'>Origin</th>");
        emailBody.append("<th style='border: 1px solid black; font-weight: bold; color: black;'>Destination</th>");
        emailBody.append("<th style='border: 1px solid black; font-weight: bold; color: black;'>Status</th></tr>");

        for (Ticket ticket : tickets) {
            emailBody.append("<tr>");
            emailBody.append("<td style='border: 1px solid black; padding: 3px 5px 3px 5px;'>").append(ticket.getId()).append("</td>");
            emailBody.append("<td style='border: 1px solid black; padding: 3px 5px;'>").append(ticket.getCreatedAt().format(dateFormatter)).append("</td>");
            emailBody.append("<td style='border: 1px solid black; padding: 3px 5px 3px 5px;'>").append(ticket.getShipperName()).append("</td>");
            emailBody.append("<td style='border: 1px solid black; padding: 3px 5px 3px 5px;'>").append(ticket.getRecipientName()).append("</td>");
            emailBody.append("<td style='border: 1px solid black; padding: 3px 5px 3px 5px;'>").append(ticket.getOriginCountry()).append(", ").append(ticket.getOriginCity()).append("</td>");
            emailBody.append("<td style='border: 1px solid black; padding: 3px 5px 3px 5px;'>").append(ticket.getDestinationCountry()).append(", ").append(ticket.getDestinationCity()).append("</td>");
            emailBody.append("<td style='border: 1px solid black; padding: 3px 5px 3px 5px;'>").append(ticket.getTicketStatus()).append("</td>");
            emailBody.append("</tr>");
        }

        emailBody.append("</table>");
        emailBody.append("</body></html>");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        try {
            helper.setFrom(sender);
            helper.setTo(toAddress);
            if (ccAddresses != null && ccAddresses.length > 0) {
                helper.setCc(ccAddresses);
            }
            helper.setSubject("Alert: Pending Tickets");
            helper.setText(emailBody.toString(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        javaMailSender.send(message);
    }

    @Async
    public void sendEmail(String toAddress, String[] ccAddresses) {
        try {
            // Get the date of the previous day
            LocalDate previousDate = LocalDate.now().minusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
            String formattedDate = previousDate.format(formatter);

            // Generate the Excel reports
            ByteArrayOutputStream pickedUpReport = excelGenerationService.generateAwbStatusReport(PICKED_UP);
            ByteArrayOutputStream deliveredReport = excelGenerationService.generateAwbStatusReport(DELIVERED);
            ByteArrayOutputStream transitStatusReport = excelGenerationService.generateAwbTransitStatusReport();

            // Create the email message
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(sender);
            helper.setTo(toAddress);
            if (ccAddresses != null && ccAddresses.length > 0) {
                helper.setCc(ccAddresses);
            }
            helper.setSubject("Daily Status Reports");

            // Set the email body with the formatted date
            String emailBody = "Gents,\n"
                    + "Pls find attached Daily Automated Report for Date: " + formattedDate + "\n\n"
                    + "Salassil System Alert\n"
                    + "Salassilexpress.com";
            helper.setText(emailBody);

            // Create ByteArrayResources from the byte arrays
            ByteArrayResource pickedUpReportResource = new ByteArrayResource(pickedUpReport.toByteArray());
            ByteArrayResource deliveredReportResource = new ByteArrayResource(deliveredReport.toByteArray());
            ByteArrayResource transitStatusReportResource = new ByteArrayResource(transitStatusReport.toByteArray());

            // Attach the reports using ByteArrayResource
            helper.addAttachment("PickedUpReport.xlsx", pickedUpReportResource);
            helper.addAttachment("DeliveredReport.xlsx", deliveredReportResource);
            helper.addAttachment("TransitStatusReport.xlsx", transitStatusReportResource);

            // Send the email
            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendBillingEmailWithAttachments(String toAddress, byte[] pdf, byte[] excel){
        try {
            // Create the email message
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(sender);
            helper.setTo(toAddress);
            helper.setSubject("Salassil Billing Report");

            // Set the email body with the formatted date
            String emailBody = "Gents,\n"
                    + "Please find attached Billing Report.\n\n"
                    + "Salassil System Alert\n"
                    + "Salassilexpress.com";
            helper.setText(emailBody);

            // Attach the billing report using ByteArrayResource
            helper.addAttachment("BillingReport.xlsx", new ByteArrayResource(excel));
            helper.addAttachment("BillingReport.pdf", new ByteArrayResource(pdf));

            // Send the email
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendPasswordResetEmail(User user, String resetCode) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(user.getEmail());
            helper.setSubject("Password Reset Request");

            String resetLink = user.getEmail() + "&code=" + resetCode;

            String emailContent = "<html><body>"
                    + "<p>Dear <strong style='color: blue;'>" + user.getName() + "</strong>,</p>"
                    + "<p>You have requested to reset your password. Please use the following code:</p>"
                    + "<p style='text-align:center ; font-size: 24px; padding: 20px; background-color: black; color: white;'>" + resetCode + "</p>"
                    + "<p>If you did not request a password reset, please ignore this email.</p>"
                    + "<p>Best regards,<br/>Salassil Team</p>"
                    + "</body></html>";

            helper.setText(emailContent, true);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    @Async
    public void sendOtpEmailForAwb(String email, String otp) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(email);
            helper.setSubject("AWB Creation OTP");

            String emailContent = "Dear User,\n\n"
                    + "To proceed with the creation of your AWB, please verify your identity using the OTP below:\n\n"
                    + "OTP: " + otp + "\n\n"
                    + "If you did not initiate this action, please contact our support team immediately.\n\n"
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
