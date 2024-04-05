package com.salsel.scheduler;

import com.salsel.utils.EmailUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class AwbStatusReportScheduler {

    private final EmailUtils emailUtils;

    public AwbStatusReportScheduler(EmailUtils emailUtils) {
        this.emailUtils = emailUtils;
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
}
