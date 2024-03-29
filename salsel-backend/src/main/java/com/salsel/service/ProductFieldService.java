package com.salsel.service;

import com.salsel.dto.ProductFieldDto;
import com.salsel.dto.ProductFieldValuesDto;
import com.salsel.model.ProductField;
import com.salsel.model.ProductFieldValues;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductFieldService {
    ProductFieldDto save(ProductFieldDto productFieldDto);
    List<ProductFieldDto> getAll();
    ProductFieldDto findById(Long id);
    ProductFieldDto findByName(String name);
    List<ProductFieldDto> searchByName(String name);
    List<ProductFieldDto> getProductFieldByProductFieldValueId(Long productFieldValueId);
    String deleteById(Long id);
    void setToActiveById(Long id);
    ProductFieldDto updatedProductField(Long id, ProductField productField);
    void deleteProductFieldValuesById(Long id, Long pfvId);
    List<ProductFieldValuesDto> getAllProductFieldValuesByProductFieldName(String productField);
}
