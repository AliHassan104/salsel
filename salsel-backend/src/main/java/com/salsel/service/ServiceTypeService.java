package com.salsel.service;

import com.salsel.dto.ServiceTypeDto;

import java.util.List;

public interface ServiceTypeService {
    ServiceTypeDto save(ServiceTypeDto serviceTypeDto);
    List<ServiceTypeDto> getAll();
    List<ServiceTypeDto> getAllByProductType(Long productTypeId);
    ServiceTypeDto findById(Long id);
    ServiceTypeDto findByCode(String code);
    void deleteById(Long id);
    ServiceTypeDto update(Long id, ServiceTypeDto serviceTypeDto);
}
