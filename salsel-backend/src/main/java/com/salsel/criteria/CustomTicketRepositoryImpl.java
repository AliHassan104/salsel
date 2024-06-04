package com.salsel.criteria;

import com.salsel.model.Ticket;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomTicketRepositoryImpl implements CustomTicketRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Ticket> findTickets(
            LocalDate startDate,
            LocalDate endDate,
            String ticketNumber,
            String ticketStatus,
            String ticketCategory,
            String ticketSubCategory,
            String department,
            String assignedTo) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class);
        Root<Ticket> ticket = query.from(Ticket.class);

        List<Predicate> predicates = new ArrayList<>();

        if (startDate != null && endDate != null) {
            predicates.add(cb.between(ticket.get("createdAt"), startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay()));
        }

        if (StringUtils.hasText(ticketNumber)) {
            predicates.add(cb.equal(ticket.get("ticketNumber"), ticketNumber));
        }

        if (StringUtils.hasText(ticketStatus)) {
            predicates.add(cb.equal(ticket.get("ticketStatus"), ticketStatus));
        }

        if (StringUtils.hasText(ticketCategory)) {
            predicates.add(cb.equal(ticket.get("ticketCategory"), ticketCategory));
        }

        if (StringUtils.hasText(ticketSubCategory)) {
            predicates.add(cb.equal(ticket.get("ticketSubCategory"), ticketSubCategory));
        }

        if (StringUtils.hasText(department)) {
            predicates.add(cb.equal(ticket.get("department"), department));
        }

        if (StringUtils.hasText(assignedTo)) {
            predicates.add(cb.equal(ticket.get("assignedTo"), assignedTo));
        }

        // Add predicate for status being true
        predicates.add(cb.isTrue(ticket.get("status")));

        query.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }
}
