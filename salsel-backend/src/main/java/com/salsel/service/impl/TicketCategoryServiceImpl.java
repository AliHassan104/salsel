package com.salsel.service.impl;

import com.salsel.dto.DepartmentCategoryDto;
import com.salsel.dto.PaginationResponse;
import com.salsel.dto.TicketCategoryDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.Department;
import com.salsel.model.DepartmentCategory;
import com.salsel.model.TicketCategory;
import com.salsel.repository.DepartmentCategoryRepository;
import com.salsel.repository.TicketCategoryRepository;
import com.salsel.service.TicketCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketCategoryServiceImpl implements TicketCategoryService {

    private final TicketCategoryRepository ticketCategoryRepository;
    private final DepartmentCategoryRepository departmentCategoryRepository;

    public TicketCategoryServiceImpl(TicketCategoryRepository ticketCategoryRepository, DepartmentCategoryRepository departmentCategoryRepository) {
        this.ticketCategoryRepository = ticketCategoryRepository;
        this.departmentCategoryRepository = departmentCategoryRepository;
    }


    @Override
    @Transactional
    public TicketCategoryDto save(TicketCategoryDto ticketCategoryDto) {
        TicketCategory ticketCategory = toEntity(ticketCategoryDto);
        ticketCategory.setStatus(true);

        DepartmentCategory departmentCategory = departmentCategoryRepository.findById(ticketCategory.getDepartmentCategory().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Department Category not found for id => %d", ticketCategory.getDepartmentCategory().getId())));

        ticketCategory.setDepartmentCategory(departmentCategory);
        TicketCategory createdTicketCategory = ticketCategoryRepository.save(ticketCategory);
        return toDto(createdTicketCategory);
    }

    @Override
    @Transactional
    public List<TicketCategoryDto> getAll(Boolean status) {

        List<TicketCategory> ticketCategoryList = ticketCategoryRepository.findAllInDesOrderByIdAndStatus(status);
        List<TicketCategoryDto> ticketCategoryDtoList = new ArrayList<>();

        for (TicketCategory ticketCategory : ticketCategoryList) {
            TicketCategoryDto ticketCategoryDto = toDto(ticketCategory);
            ticketCategoryDtoList.add(ticketCategoryDto);
        }
        return ticketCategoryDtoList;
    }

    @Override
    @Transactional
    public PaginationResponse getAllPaginatedTicketCategory(Integer pageNumber, Integer pageSize) {

        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<TicketCategory> ticketCategoryPage = ticketCategoryRepository.findAllInDesOrderByIdAndStatus(page);
        List<TicketCategory> ticketCategoryList = ticketCategoryPage.getContent();

        List<TicketCategoryDto> ticketCategoryDtoList = new ArrayList<>();
        for (TicketCategory ticketCategory : ticketCategoryList) {
            TicketCategoryDto ticketCategoryDto = toDto(ticketCategory);
            ticketCategoryDtoList.add(ticketCategoryDto);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(ticketCategoryDtoList);
        paginationResponse.setPageNumber(ticketCategoryPage.getNumber());
        paginationResponse.setPageSize(ticketCategoryPage.getSize());
        paginationResponse.setTotalElements(ticketCategoryPage.getNumberOfElements());
        paginationResponse.setTotalPages(ticketCategoryPage.getTotalPages());
        paginationResponse.setLastPage(ticketCategoryPage.isLast());

        return paginationResponse;
    }

    @Override
    @Transactional
    public PaginationResponse searchByName(String name, Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<TicketCategory> ticketCategoryPage = ticketCategoryRepository.findDepartmentCategoryByName(name,page);
        List<TicketCategory> ticketCategoryList = ticketCategoryPage.getContent();

        List<TicketCategoryDto> ticketCategoryDtoList = new ArrayList<>();
        for (TicketCategory ticketCategory : ticketCategoryList) {
            TicketCategoryDto ticketCategoryDto = toDto(ticketCategory);
            ticketCategoryDtoList.add(ticketCategoryDto);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(ticketCategoryDtoList);
        paginationResponse.setPageNumber(ticketCategoryPage.getNumber());
        paginationResponse.setPageSize(ticketCategoryPage.getSize());
        paginationResponse.setTotalElements(ticketCategoryPage.getNumberOfElements());
        paginationResponse.setTotalPages(ticketCategoryPage.getTotalPages());
        paginationResponse.setLastPage(ticketCategoryPage.isLast());

        return paginationResponse;
    }

    @Override
    @Transactional
    public List<TicketCategoryDto> getAllByDepartmentCategory(Long departmentCategoryId) {
        List<TicketCategory> ticketCategoryList = ticketCategoryRepository.findAllByDepartmentCategoryWhereStatusIsTrue(departmentCategoryId);
        List<TicketCategoryDto> ticketCategoryDtoList = new ArrayList<>();

        for (TicketCategory ticketCategory : ticketCategoryList) {
            TicketCategoryDto ticketCategoryDto = toDto(ticketCategory);
            ticketCategoryDtoList.add(ticketCategoryDto);
        }
        return ticketCategoryDtoList;
    }

    @Override
    @Transactional
    public TicketCategoryDto findById(Long id)
    {
        TicketCategory ticketCategory = ticketCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketCategory not found for id => %d", id)));
        return toDto(ticketCategory);
    }

    @Override
    @Transactional
    public TicketCategoryDto findByName(String name) {
        TicketCategory ticketCategory = ticketCategoryRepository.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketCategory not found for name => %s", name)));
        return toDto(ticketCategory);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        TicketCategory ticketCategory = ticketCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketCategory not found for id => %d", id)));
        ticketCategoryRepository.setStatusInactive(ticketCategory.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        TicketCategory ticketCategory = ticketCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketCategory not found for id => %d", id)));
        ticketCategoryRepository.setStatusActive(ticketCategory.getId());
    }

    @Override
    @Transactional
    public TicketCategoryDto update(Long id, TicketCategoryDto ticketCategoryDto) {
        TicketCategory existingTicketCategory = ticketCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketCategory not found for id => %d", id)));

        existingTicketCategory.setName(ticketCategoryDto.getName());

        existingTicketCategory.setDepartmentCategory(departmentCategoryRepository.findById(ticketCategoryDto.getDepartmentCategory().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Department Category not found for id => %d", ticketCategoryDto.getDepartmentCategory().getId()))));

        TicketCategory updatedTicketCategory = ticketCategoryRepository.save(existingTicketCategory);
        return toDto(updatedTicketCategory);
    }

    public TicketCategoryDto toDto(TicketCategory ticketCategory) {
        return TicketCategoryDto.builder()
                .id(ticketCategory.getId())
                .name(ticketCategory.getName())
                .status(ticketCategory.getStatus())
                .departmentCategory(ticketCategory.getDepartmentCategory())
                .build();
    }

    public TicketCategory toEntity(TicketCategoryDto ticketCategoryDto) {
        return TicketCategory.builder()
                .id(ticketCategoryDto.getId())
                .name(ticketCategoryDto.getName())
                .status(ticketCategoryDto.getStatus())
                .departmentCategory(ticketCategoryDto.getDepartmentCategory())
                .build();
    }
}
