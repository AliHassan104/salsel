package com.salsel.service;

import com.salsel.dto.DepartmentCategoryDto;
import com.salsel.dto.PaginationResponse;
import com.salsel.dto.TicketCategoryDto;

import java.util.List;

public interface TicketCategoryService {
    TicketCategoryDto save(TicketCategoryDto ticketCategoryDto);
    List<TicketCategoryDto> getAll(Boolean status);
    PaginationResponse getAllPaginatedTicketCategory(Integer pageNumber, Integer pageSize);
    PaginationResponse searchByName(String name, Integer pageNumber, Integer pageSize);
    List<TicketCategoryDto> getAllByDepartmentCategory(Long departmentCategoryId);

    TicketCategoryDto findById(Long id);
    TicketCategoryDto findByName(String name);
    void deleteById(Long id);
    void setToActiveById(Long id);
    TicketCategoryDto update(Long id, TicketCategoryDto ticketCategoryDto);
}
