package com.salsel.service.impl;

import com.salsel.dto.ServiceTypeDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.ServiceType;
import com.salsel.repository.ProductTypeRepository;
import com.salsel.repository.ServiceTypeRepository;
import com.salsel.service.ServiceTypeService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {

    private final ProductTypeRepository productTypeRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeServiceImpl(ProductTypeRepository productTypeRepository, ServiceTypeRepository serviceTypeRepository) {
        this.productTypeRepository = productTypeRepository;
        this.serviceTypeRepository = serviceTypeRepository;
    }

    @Override
    @Transactional
    public ServiceTypeDto save(ServiceTypeDto serviceTypeDto) {
        ServiceType serviceType = toEntity(serviceTypeDto);
        serviceType.setStatus(true);

        serviceType.setProductType(productTypeRepository.findById(serviceType.getProductType().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("ProductType not found for id => %d", serviceType.getProductType().getId()))));

        ServiceType createdServiceType = serviceTypeRepository.save(serviceType);
        return toDto(createdServiceType);
    }

    @Override
    public List<ServiceTypeDto> getAll(Boolean status) {
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAllInDesOrderByIdAndStatus(status);
        List<ServiceTypeDto> serviceTypeDtoList = new ArrayList<>();

        for (ServiceType serviceType : serviceTypeList) {
            ServiceTypeDto serviceTypeDto = toDto(serviceType);
            serviceTypeDtoList.add(serviceTypeDto);
        }
        return serviceTypeDtoList;
    }

    @Override
    public ServiceTypeDto findById(Long id) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("ServiceType not found for id => %d", id)));
        return toDto(serviceType);
    }

    @Override
    public ServiceTypeDto findByCode(String code) {
        ServiceType serviceType = serviceTypeRepository.findByCode(code)
                .orElseThrow(() -> new RecordNotFoundException(String.format("ServiceType not found for code => %s", code)));
        return toDto(serviceType);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("ServiceType not found for id => %d", id)));
        serviceTypeRepository.setStatusInactive(serviceType.getId());
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("ServiceType not found for id => %d", id)));
        serviceTypeRepository.setStatusActive(serviceType.getId());
    }

    @Override
    @Transactional
    public ServiceTypeDto update(Long id, ServiceTypeDto serviceTypeDto) {
        ServiceType existingServiceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("ServiceType not found for id => %d", id)));

        existingServiceType.setName(serviceTypeDto.getName());
        existingServiceType.setCode(serviceTypeDto.getCode());

        existingServiceType.setProductType(productTypeRepository.findById(serviceTypeDto.getProductType().getId())
                .orElseThrow(() -> new RecordNotFoundException(String.format("ProductType not found for id => %d", serviceTypeDto.getProductType().getId()))));

        ServiceType updatedServiceType = serviceTypeRepository.save(existingServiceType);
        return toDto(updatedServiceType);
    }

    public ServiceTypeDto toDto(ServiceType serviceType) {
        return ServiceTypeDto.builder()
                .id(serviceType.getId())
                .code(serviceType.getCode())
                .name(serviceType.getName())
                .status(serviceType.getStatus())
                .productType(serviceType.getProductType())
                .build();
    }

    public ServiceType toEntity(ServiceTypeDto serviceTypeDto) {
        return ServiceType.builder()
                .id(serviceTypeDto.getId())
                .code(serviceTypeDto.getCode())
                .name(serviceTypeDto.getName())
                .status(serviceTypeDto.getStatus())
                .productType(serviceTypeDto.getProductType())
                .build();
    }
}
