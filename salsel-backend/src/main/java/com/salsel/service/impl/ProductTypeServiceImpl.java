package com.salsel.service.impl;

import com.salsel.dto.ProductTypeDto;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.ProductType;
import com.salsel.repository.ProductTypeRepository;
import com.salsel.service.ProductTypeService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    @Transactional
    public ProductTypeDto save(ProductTypeDto productTypeDto) {
        ProductType productType = toEntity(productTypeDto);
        productType.setStatus(true);

        ProductType createdProductType = productTypeRepository.save(productType);
        return toDto(createdProductType);
    }

    @Override
    public List<ProductTypeDto> getAll() {
        List<ProductType> productTypeList = productTypeRepository.findAllInDesOrderByIdAndStatus();
        List<ProductTypeDto> productTypeDtoList = new ArrayList<>();

        for (ProductType productType : productTypeList) {
            ProductTypeDto productTypeDto = toDto(productType);
            productTypeDtoList.add(productTypeDto);
        }
        return productTypeDtoList;
    }

    @Override
    public ProductTypeDto findById(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("ProductType not found for id => %d", id)));
        return toDto(productType);
    }

    @Override
    public ProductTypeDto findByCode(String code) {
        ProductType productType = productTypeRepository.findByCode(code)
                .orElseThrow(() -> new RecordNotFoundException(String.format("ProductType not found for code => %s", code)));
        return toDto(productType);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("ProductType not found for id => %d", id)));
        productTypeRepository.setStatusInactive(productType.getId());
    }

    @Override
    @Transactional
    public ProductTypeDto update(Long id, ProductTypeDto productTypeDto) {
        ProductType existingProductType = productTypeRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(String.format("ProductType not found for id => %d", id)));

        existingProductType.setName(productTypeDto.getName());
        existingProductType.setCode(productTypeDto.getCode());

        ProductType updatedProductType = productTypeRepository.save(existingProductType);
        return toDto(updatedProductType);
    }

    public ProductTypeDto toDto(ProductType productType) {
        return ProductTypeDto.builder()
                .id(productType.getId())
                .code(productType.getCode())
                .name(productType.getName())
                .status(productType.getStatus())
                .build();
    }

    public ProductType toEntity(ProductTypeDto productTypeDto) {
        return ProductType.builder()
                .id(productTypeDto.getId())
                .code(productTypeDto.getCode())
                .name(productTypeDto.getName())
                .status(productTypeDto.getStatus())
                .build();
    }
}
