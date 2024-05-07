package com.salsel.scheduler;

import com.salsel.model.Ticket;
import com.salsel.repository.TicketRepository;
import com.salsel.utils.EmailUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class EmailScheduler {
    private final EmailUtils emailUtils;
    private final TicketRepository ticketRepository;

    public EmailScheduler(EmailUtils emailUtils, TicketRepository ticketRepository) {
        this.emailUtils = emailUtils;
        this.ticketRepository = ticketRepository;
    }

    @Scheduled(cron = "0 5 0 * * ?") // Execute every day at 12:05 AM
    public void sendDailyEmailReport() {
        // Get the current date and time
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Check if it's after 5 minutes past midnight
        if (currentTime.isAfter(LocalTime.of(0, 5))) {
            // Increment the current date to the next day
            currentDate = currentDate.plusDays(1);
        }

        // Construct the  recipient email addresses
        String toAddress = "a.choudhary@salassilexpress.com";
        String[] ccAddresses = {"ashraf@salassilexpress.com", "samer@salassilexpress.com", "muhammadtabish05@gmail.com"};

        // Send the email
        emailUtils.sendEmail(toAddress, ccAddresses);

        // Log or handle success/failure
        System.out.println("Daily email report sent for " + currentDate);
    }

    @Scheduled(cron = "0 6 0 * * ?") // Execute every day at 12:06 AM
    public void sendEmailForPendingTickets() {
        List<Ticket> pendingTickets = ticketRepository.getAllTicketsWhereStatusIsNotClosed();
        if(!pendingTickets.isEmpty()){
            String toAddress = "a.choudhary@salassilexpress.com";
            String[] ccAddresses = {"ashraf@salassilexpress.com", "samer@salassilexpress.com", "muhammadtabish05@gmail.com"};
            try {
                emailUtils.sendAlertEmailForPendingTickets(pendingTickets, toAddress, ccAddresses);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Scheduled(cron = "0 6 0 * * ?") // Execute every day at 4:40 PM
    public void sendEmailForBillingReport() {
            String toAddress = "usmankhann13888@mailddrop.cc";
            String[] ccAddresses = {"usmankhann13777@maildrop.cc"};
        emailUtils.sendBillingEmail(toAddress, ccAddresses);

        System.out.println("Daily email report sent for Billing");
    }
}
