package com.salsel.service.impl;

import com.salsel.dto.TicketCommentsDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.TicketComments;
import com.salsel.repository.TicketCommentsRepository;
import com.salsel.repository.TicketRepository;
import com.salsel.service.TicketCommentsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketCommentsServiceImpl implements TicketCommentsService {
    private final TicketCommentsRepository ticketCommentsRepository;
    private final TicketRepository ticketRepository;

    public TicketCommentsServiceImpl(TicketCommentsRepository ticketCommentsRepository, TicketRepository ticketRepository) {
        this.ticketCommentsRepository = ticketCommentsRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    @Transactional
    public TicketCommentsDto save(TicketCommentsDto ticketCommentsDto) {
        TicketComments ticketComments = toEntity(ticketCommentsDto);
        ticketComments.setStatus(true);

        ticketComments.setTicket(ticketRepository.findById(ticketComments.getTicket().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Ticket not found for id => %d", ticketComments.getTicket().getId()))));

        TicketComments createdTicketComments = ticketCommentsRepository.save(ticketComments);
        return toDto(createdTicketComments);
    }

    @Override
    public List<TicketCommentsDto> getAll(Boolean status) {
        List<TicketComments> ticketCommentsList = ticketCommentsRepository.findAllInDesOrderByIdAndStatus(status);
        List<TicketCommentsDto> ticketCommentsDtoList = new ArrayList<>();

        for (TicketComments ticketComments : ticketCommentsList) {
            TicketCommentsDto ticketCommentsDto = toDto(ticketComments);
            ticketCommentsDtoList.add(ticketCommentsDto);
        }
        return ticketCommentsDtoList;
    }

    @Override
    public TicketCommentsDto findById(Long id) {
        TicketComments ticketComments = ticketCommentsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketComments not found for id => %d", id)));
        return toDto(ticketComments);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        TicketComments ticketComments = ticketCommentsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketComments not found for id => %d", id)));
        ticketCommentsRepository.setStatusInactive(ticketComments.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        TicketComments ticketComments = ticketCommentsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketComments not found for id => %d", id)));
        ticketCommentsRepository.setStatusActive(ticketComments.getId());
    }

    @Override
    @Transactional
    public TicketCommentsDto update(Long id, TicketCommentsDto ticketCommentsDto) {
        TicketComments existingTicketComments = ticketCommentsRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketComments not found for id => %d", id)));

        existingTicketComments.setComment(ticketCommentsDto.getComment());
        existingTicketComments.setMessage(ticketCommentsDto.getMessage());

        existingTicketComments.setTicket(ticketRepository.findById(ticketCommentsDto.getTicket().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketComments not found for id => %d", ticketCommentsDto.getTicket().getId()))));

        TicketComments updatedTicketComments = ticketCommentsRepository.save(existingTicketComments);
        return toDto(updatedTicketComments);
    }

    public TicketCommentsDto toDto(TicketComments ticketComments) {
        return TicketCommentsDto.builder()
                .id(ticketComments.getId())
                .timestamp(ticketComments.getTimestamp())
                .comment(ticketComments.getComment())
                .message(ticketComments.getMessage())
                .status(ticketComments.getStatus())
                .ticket(ticketComments.getTicket())
                .build();
    }

    public TicketComments toEntity(TicketCommentsDto ticketCommentsDto) {
        return TicketComments.builder()
                .id(ticketCommentsDto.getId())
                .timestamp(ticketCommentsDto.getTimestamp())
                .comment(ticketCommentsDto.getComment())
                .message(ticketCommentsDto.getMessage())
                .status(ticketCommentsDto.getStatus())
                .ticket(ticketCommentsDto.getTicket())
                .build();
    }
}
