package com.salsel.service.impl;

import com.salsel.dto.PaginationResponse;
import com.salsel.dto.TicketCategoryDto;
import com.salsel.dto.TicketSubCategoryDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.DepartmentCategory;
import com.salsel.model.TicketCategory;
import com.salsel.model.TicketSubCategory;
import com.salsel.repository.TicketCategoryRepository;
import com.salsel.repository.TicketSubCategoryRepository;
import com.salsel.service.TicketSubCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketSubCategoryServiceImpl implements TicketSubCategoryService {

    private final TicketSubCategoryRepository ticketSubCategoryRepository;
    private final TicketCategoryRepository ticketCategoryRepository;

    public TicketSubCategoryServiceImpl(TicketSubCategoryRepository ticketSubCategoryRepository, TicketCategoryRepository ticketCategoryRepository) {
        this.ticketSubCategoryRepository = ticketSubCategoryRepository;
        this.ticketCategoryRepository = ticketCategoryRepository;
    }


    @Override
    @Transactional
    public TicketSubCategoryDto save(TicketSubCategoryDto ticketSubCategoryDto) {
        TicketSubCategory ticketSubCategory = toEntity(ticketSubCategoryDto);
        ticketSubCategory.setStatus(true);

        TicketCategory ticketCategory = ticketCategoryRepository.findById(ticketSubCategory.getTicketCategory().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Ticket Category not found for id => %d", ticketSubCategory.getTicketCategory().getId())));

        ticketSubCategory.setTicketCategory(ticketCategory);
        TicketSubCategory createdTicketSubCategory = ticketSubCategoryRepository.save(ticketSubCategory);
        return toDto(createdTicketSubCategory);
    }

    @Override
    @Transactional
    public List<TicketSubCategoryDto> getAll(Boolean status) {
        List<TicketSubCategory> ticketSubCategoryList = ticketSubCategoryRepository.findAllInDesOrderByIdAndStatus(status);
        List<TicketSubCategoryDto> ticketSubCategoryDtoList = new ArrayList<>();

        for (TicketSubCategory ticketSubCategory : ticketSubCategoryList) {
            TicketSubCategoryDto ticketSubCategoryDto = toDto(ticketSubCategory);
            ticketSubCategoryDtoList.add(ticketSubCategoryDto);
        }
        return ticketSubCategoryDtoList;
    }

    @Override
    @Transactional
    public PaginationResponse getAllPaginatedTicketCategory(Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<TicketSubCategory> ticketSubCategoryPage = ticketSubCategoryRepository.findAllInDesOrderByIdAndStatus(page);
        List<TicketSubCategory> ticketSubCategoryList = ticketSubCategoryPage.getContent();

        List<TicketSubCategoryDto> ticketSubCategoryDtoList = new ArrayList<>();
        for (TicketSubCategory ticketSubCategory : ticketSubCategoryList) {
            TicketSubCategoryDto ticketSubCategoryDto = toDto(ticketSubCategory);
            ticketSubCategoryDtoList.add(ticketSubCategoryDto);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(ticketSubCategoryDtoList);
        paginationResponse.setPageNumber(ticketSubCategoryPage.getNumber());
        paginationResponse.setPageSize(ticketSubCategoryPage.getSize());
        paginationResponse.setTotalElements(ticketSubCategoryPage.getNumberOfElements());
        paginationResponse.setTotalPages(ticketSubCategoryPage.getTotalPages());
        paginationResponse.setLastPage(ticketSubCategoryPage.isLast());

        return paginationResponse;
    }

    @Override
    @Transactional
    public PaginationResponse searchByName(String name, Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<TicketSubCategory> ticketSubCategoryPage = ticketSubCategoryRepository.findTicketSubCategoryByName(name,page);
        List<TicketSubCategory> ticketSubCategoryList = ticketSubCategoryPage.getContent();

        List<TicketSubCategoryDto> ticketSubCategoryDtoList = new ArrayList<>();
        for (TicketSubCategory ticketSubCategory : ticketSubCategoryList) {
            TicketSubCategoryDto ticketSubCategoryDto = toDto(ticketSubCategory);
            ticketSubCategoryDtoList.add(ticketSubCategoryDto);
        }

        PaginationResponse paginationResponse = new PaginationResponse();
        paginationResponse.setContent(ticketSubCategoryDtoList);
        paginationResponse.setPageNumber(ticketSubCategoryPage.getNumber());
        paginationResponse.setPageSize(ticketSubCategoryPage.getSize());
        paginationResponse.setTotalElements(ticketSubCategoryPage.getNumberOfElements());
        paginationResponse.setTotalPages(ticketSubCategoryPage.getTotalPages());
        paginationResponse.setLastPage(ticketSubCategoryPage.isLast());

        return paginationResponse;
    }

    @Override
    @Transactional
    public List<TicketSubCategoryDto> getAllByTicketCategory(Long ticketCategoryId) {
        List<TicketSubCategory> ticketSubCategoryList = ticketSubCategoryRepository.findAllByTicketCategoryWhereStatusIsTrue(ticketCategoryId);
        List<TicketSubCategoryDto> ticketSubCategoryDtoList = new ArrayList<>();

        for (TicketSubCategory ticketSubCategory : ticketSubCategoryList) {
            TicketSubCategoryDto ticketSubCategoryDto = toDto(ticketSubCategory);
            ticketSubCategoryDtoList.add(ticketSubCategoryDto);
        }
        return ticketSubCategoryDtoList;
    }

    @Override
    @Transactional
    public TicketSubCategoryDto findById(Long id) {
        TicketSubCategory ticketSubCategory = ticketSubCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Ticket Sub Category not found for id => %d", id)));
        return toDto(ticketSubCategory);
    }

    @Override
    @Transactional
    public TicketSubCategoryDto findByName(String name) {
        TicketSubCategory ticketSubCategory = ticketSubCategoryRepository.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketSubCategory not found for name => %s", name)));
        return toDto(ticketSubCategory);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        TicketSubCategory ticketSubCategory = ticketSubCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketSubCategory not found for id => %d", id)));
        ticketSubCategoryRepository.setStatusInactive(ticketSubCategory.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        TicketSubCategory ticketSubCategory = ticketSubCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketSubCategory not found for id => %d", id)));
        ticketSubCategoryRepository.setStatusActive(ticketSubCategory.getId());
    }

    @Override
    @Transactional
    public TicketSubCategoryDto update(Long id, TicketSubCategoryDto ticketSubCategoryDto) {
        TicketSubCategory existingTicketSubCategory = ticketSubCategoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("TicketSubCategory not found for id => %d", id)));

        existingTicketSubCategory.setName(ticketSubCategoryDto.getName());

        existingTicketSubCategory.setTicketCategory(ticketCategoryRepository.findById(ticketSubCategoryDto.getTicketCategory().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("Ticket Sub Category not found for id => %d", ticketSubCategoryDto.getTicketCategory().getId()))));

        TicketSubCategory updatedTicketSubCategory = ticketSubCategoryRepository.save(existingTicketSubCategory);
        return toDto(updatedTicketSubCategory);
    }

    public TicketSubCategoryDto toDto(TicketSubCategory ticketSubCategory) {
        return TicketSubCategoryDto.builder()
                .id(ticketSubCategory.getId())
                .name(ticketSubCategory.getName())
                .status(ticketSubCategory.getStatus())
                .ticketCategory(ticketSubCategory.getTicketCategory())
                .build();
    }

    public TicketSubCategory toEntity(TicketSubCategoryDto ticketSubCategoryDto) {
        return TicketSubCategory.builder()
                .id(ticketSubCategoryDto.getId())
                .name(ticketSubCategoryDto.getName())
                .status(ticketSubCategoryDto.getStatus())
                .ticketCategory(ticketSubCategoryDto.getTicketCategory())
                .build();
    }
}
