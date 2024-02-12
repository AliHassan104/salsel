package com.salsel.service;

import com.salsel.dto.PaginationResponse;
import com.salsel.dto.TicketCategoryDto;
import com.salsel.dto.TicketSubCategoryDto;

import java.util.List;

public interface TicketSubCategoryService {
    TicketSubCategoryDto save(TicketSubCategoryDto ticketSubCategoryDto);
    List<TicketSubCategoryDto> getAll(Boolean status);
    PaginationResponse getAllPaginatedTicketCategory(Integer pageNumber, Integer pageSize);
    PaginationResponse searchByName(String name, Integer pageNumber, Integer pageSize);
    List<TicketSubCategoryDto> getAllByTicketCategory(Long ticketCategoryId);

    TicketSubCategoryDto findById(Long id);
    TicketSubCategoryDto findByName(String name);
    void deleteById(Long id);
    void setToActiveById(Long id);
    TicketSubCategoryDto update(Long id, TicketSubCategoryDto ticketSubCategoryDto);
}
