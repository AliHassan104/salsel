package com.salsel.constants;
import com.salsel.model.Ticket;

import java.lang.reflect.Field;
import java.util.Arrays;

public class TicketConstant {
    public static String[] ticketModels;

    static {
        // Use reflection to get field names from TicketDto class
        Field[] fields = Ticket.class.getDeclaredFields();
        ticketModels = Arrays.stream(fields)
                .map(Field::getName)
                .toArray(String[]::new);
    }
}
