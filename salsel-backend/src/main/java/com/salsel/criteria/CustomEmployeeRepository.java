package com.salsel.criteria;

import com.salsel.model.Employee;
import com.salsel.model.Ticket;

import java.time.LocalDate;
import java.util.List;

public interface CustomEmployeeRepository {

    List<Employee> findEmployee(
            String department,
            String country
    );
}
