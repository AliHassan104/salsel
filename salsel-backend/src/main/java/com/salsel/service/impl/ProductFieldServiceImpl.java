package com.salsel.service.impl;

import com.salsel.dto.ProductFieldDto;
import com.salsel.dto.ProductFieldValuesDto;
import com.salsel.dto.projectEnums.Type;
import com.salsel.exception.RecordNotFoundException;
import com.salsel.model.ProductField;
import com.salsel.model.ProductFieldValues;
import com.salsel.repository.ProductFieldRepository;
import com.salsel.repository.ProductFieldValuesRepository;
import com.salsel.service.ProductFieldService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductFieldServiceImpl implements ProductFieldService {
    private final ProductFieldRepository productFieldRepository;
    private final ProductFieldValuesRepository productFieldValuesRepository;

    public ProductFieldServiceImpl(ProductFieldRepository productFieldRepository, ProductFieldValuesRepository productFieldValuesRepository) {
        this.productFieldRepository = productFieldRepository;
        this.productFieldValuesRepository = productFieldValuesRepository;
    }


    @Transactional
    @Override
    public ProductFieldDto save(ProductFieldDto productFieldDto) {
        if(productFieldDto.getCreated_at() == null) {
            productFieldDto.setCreated_at(LocalDate.now());
        }
        if (Type.TOGGLE.equals(productFieldDto.getType()) || Type.TEXTFIELD.equals(productFieldDto.getType())) {
            productFieldDto.getProductFieldValuesList().clear();
        }
        ProductField productField = toEntity(productFieldDto);
        ProductField createdProductField = productFieldRepository.save(productField);

        List<ProductFieldValues> productFieldValuesList = productField.getProductFieldValuesList();
        if(productFieldValuesList != null && !productFieldValuesList.isEmpty()){
            for (ProductFieldValues productFieldValues : productFieldValuesList) {
                productFieldValues.setProductField(createdProductField);
                productFieldValuesRepository.save(productFieldValues);
            }
            createdProductField.setProductFieldValuesList(productFieldValuesList);
            productFieldRepository.save(createdProductField);
        }
        return toDto(createdProductField);
    }

    @Override
    public List<ProductFieldDto> getAll() {
        List<ProductField> productFieldList = productFieldRepository.findAllByStatusOrderBySequenceAsc("Active");
        List<ProductFieldDto> productFieldDtoList = new ArrayList<>();

        for (ProductField productField : productFieldList) {
            ProductFieldDto productFieldDto = toDto(productField);
            productFieldDtoList.add(productFieldDto);
        }
        return productFieldDtoList;
    }

    @Override
    public ProductFieldDto findById(Long id){
        Optional<ProductField> optionalProductField = productFieldRepository.findById(id);

        if(optionalProductField.isPresent()) {
            ProductField productField = optionalProductField.get();
            return toDto(productField);
        }
        else {
            throw new RecordNotFoundException(String.format("Product Field not found for id => %d", id));
        }
    }

    @Override
    public ProductFieldDto findByName(String name) {
        Optional<ProductField> productFieldOptional = Optional.ofNullable(productFieldRepository.findByName(name));

        if(productFieldOptional.isPresent()){
            ProductField productField = productFieldOptional.get();
            return toDto(productField);
        }
        else {
            throw new RecordNotFoundException(String.format("ProductField not found at => %s", name));
        }
    }

    @Override
    public List<ProductFieldDto> searchByName(String name) {
        List<ProductField> productFieldList = productFieldRepository.findProductFieldsByName(name);
        List<ProductFieldDto> productFieldDtoList = new ArrayList<>();

        for (ProductField productField : productFieldList) {
            ProductFieldDto productFieldDto = toDto(productField);
            productFieldDtoList.add(productFieldDto);
        }
        return productFieldDtoList;
    }

    @Override
    public List<ProductFieldDto> getProductFieldByProductFieldValueId(Long productFieldValueId) {
        Optional<List<ProductField>> optionalProductFieldList = Optional.ofNullable(productFieldRepository.findByProductFieldValuesList_Id(productFieldValueId));
        if(optionalProductFieldList.isPresent()){
            List<ProductField> productFieldList = optionalProductFieldList.get();
            List<ProductFieldDto> productFieldDtoList = new ArrayList<>();

            for (ProductField productField : productFieldList) {
                ProductFieldDto productFieldDto = toDto(productField);
                productFieldDtoList.add(productFieldDto);
            }
            return productFieldDtoList;
        } else{
            throw new RecordNotFoundException(String.format("ProductField not found on Product Field Value id => %d", productFieldValueId));
        }
    }
    @Transactional
    @Override
    public String deleteById(Long id) {
        Optional<ProductField> optionalProductField = productFieldRepository.findById(id);

        if(optionalProductField.isPresent()) {
            ProductField productField = optionalProductField.get();
            productFieldRepository.setStatusInactive(id);;
        }
        else {
            throw new RecordNotFoundException(String.format("Product Field not found for id => %d", id));
        }
        return null;
    }

    @Override
    @Transactional
    public void setToActiveById(Long id) {
        Optional<ProductField> optionalProductField = productFieldRepository.findById(id);

        if(optionalProductField.isPresent()) {
            ProductField productField = optionalProductField.get();
            productFieldRepository.setStatusActive(id);;
        }
        else {
            throw new RecordNotFoundException(String.format("Product Field not found for id => %d", id));
        }
    }

    @Transactional
    @Override
    public ProductFieldDto updatedProductField(Long id, ProductField productField) {
        Optional<ProductField> optionalProductField = productFieldRepository.findById(id);

        if (optionalProductField.isPresent()) {
            ProductField existingPf = optionalProductField.get();
            existingPf.setName(productField.getName());
            existingPf.setType(productField.getType());
            existingPf.setSequence(productField.getSequence());

            if (Type.TOGGLE.equals(productField.getType()) || Type.TEXTFIELD.equals(productField.getType())) {
                for(ProductFieldValues productFieldValues : existingPf.getProductFieldValuesList()){
                    productFieldValuesRepository.deleteById(productFieldValues.getId());
                }
                existingPf.getProductFieldValuesList().clear();
            } else {
                List<ProductFieldValues> existingPfValues = existingPf.getProductFieldValuesList();
                List<ProductFieldValues> newPfValues = productField.getProductFieldValuesList();
                List<ProductFieldValues> newValuesToAdd = new ArrayList<>();

                for (ProductFieldValues newValue : newPfValues) {
                    Optional<ProductFieldValues> existingValue = existingPfValues.stream()
                            .filter(pfValue -> pfValue.getId().equals(newValue.getId())).findFirst();
                    if (existingValue.isPresent()) {
                        ProductFieldValues existingPfValue = existingValue.get();
                        existingPfValue.setName(newValue.getName());
                        existingPfValue.setStatus(newValue.getStatus());
                    } else {
                        newValue.setProductField(existingPf);
                        newValuesToAdd.add(newValue);
                    }
                }

                existingPfValues.addAll(newValuesToAdd);
            }

            ProductField updatedPf = productFieldRepository.save(existingPf);
            return toDto(updatedPf);
        } else {
            throw new RecordNotFoundException(String.format("Product Field not found for id => %d", id));
        }
    }


    @Override
    public void deleteProductFieldValuesById(Long id, Long pfvId) {
        Optional<ProductField> optionalProductField = productFieldRepository.findById(id);
        if (optionalProductField.isPresent()) {
            ProductField productField = optionalProductField.get();

            // Find the ProductFieldValues entity with the provided pfvId
            Optional<ProductFieldValues> optionalProductFieldValues = productField.getProductFieldValuesList()
                    .stream()
                    .filter(pfv -> pfv.getId().equals(pfvId))
                    .findFirst();

            if (optionalProductFieldValues.isPresent()) {
                ProductFieldValues productFieldValuesToDelete = optionalProductFieldValues.get();
                // Remove the ProductFieldValues entity from the list
                productField.getProductFieldValuesList().remove(productFieldValuesToDelete);

                // Delete the ProductFieldValues from the database using the repository
                productFieldValuesRepository.delete(productFieldValuesToDelete);

                // Save the updated ProductField entity to reflect the changes in the database
                productFieldRepository.save(productField);
            } else{
                throw new RecordNotFoundException("Product Field Value not found");
            }
        } else {
            throw new RecordNotFoundException(String.format("Product Field not found for id => %d", id));

        }
    }


    public ProductFieldDto toDto(ProductField productField) {
        List<ProductFieldValuesDto> productFieldValuesDtoList = new ArrayList<>();
        for (ProductFieldValues productFieldValues : productField.getProductFieldValuesList()) {
            ProductFieldValuesDto dto = ProductFieldValuesDto.builder()
                    .id(productFieldValues.getId())
                    .name(productFieldValues.getName())
                    .status(productFieldValues.getStatus())
                    .build();
            productFieldValuesDtoList.add(dto);
        }

        return ProductFieldDto.builder()
                .id(productField.getId())
                .name(productField.getName())
                .status(productField.getStatus())
                .created_at(productField.getCreated_at())
                .sequence(productField.getSequence())
                .type(productField.getType())
                .productFieldValuesList(productFieldValuesDtoList)
                .build();
    }

    public ProductField toEntity(ProductFieldDto productFieldDto) {
        ProductField productField = ProductField.builder()
                .id(productFieldDto.getId())
                .name(productFieldDto.getName())
                .status(productFieldDto.getStatus())
                .created_at(productFieldDto.getCreated_at())
                .sequence(productFieldDto.getSequence())
                .type(productFieldDto.getType())
                .build();

        List<ProductFieldValues> productFieldValuesList = new ArrayList<>();
        for (ProductFieldValuesDto dto : productFieldDto.getProductFieldValuesList()) {
            ProductFieldValues productFieldValues = ProductFieldValues.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .status(dto.getStatus())
                    .build();

            productFieldValuesList.add(productFieldValues);
        }

        productField.setProductFieldValuesList(productFieldValuesList);
        return productField;
    }

}
