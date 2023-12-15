package com.salsel.controller;

import com.salsel.dto.ProductTypeDto;
import com.salsel.service.ProductTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductTypeController {
    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @PostMapping("/product-type")
    @PreAuthorize("hasAuthority('CREATE_PRODUCT_TYPE') and hasAuthority('READ_PRODUCT_TYPE')")
    public ResponseEntity<ProductTypeDto> createProductType(@RequestBody ProductTypeDto productTypeDto) {
        return ResponseEntity.ok(productTypeService.save(productTypeDto));
    }

    @GetMapping("/product-type")
    @PreAuthorize("hasAuthority('READ_PRODUCT_TYPE')")
    public ResponseEntity<List<ProductTypeDto>> getAllProductType(@RequestParam(value = "status") Boolean status) {
        List<ProductTypeDto> productTypeDtoList = productTypeService.getAll(status);
        return ResponseEntity.ok(productTypeDtoList);
    }

    @GetMapping("/product-type/{id}")
    @PreAuthorize("hasAuthority('READ_PRODUCT_TYPE')")
    public ResponseEntity<ProductTypeDto> getProductTypeById(@PathVariable Long id) {
        ProductTypeDto productTypeDto = productTypeService.findById(id);
        return ResponseEntity.ok(productTypeDto);
    }

    @GetMapping("/product-type/code/{code}")
    @PreAuthorize("hasAuthority('READ_PRODUCT_TYPE')")
    public ResponseEntity<ProductTypeDto> getProductTypeByCode(@PathVariable String code) {
        ProductTypeDto productTypeDto = productTypeService.findByCode(code);
        return ResponseEntity.ok(productTypeDto);
    }

    @DeleteMapping("/product-type/{id}")
    @PreAuthorize("hasAuthority('DELETE_PRODUCT_TYPE') and hasAuthority('READ_PRODUCT_TYPE')")
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) {
        productTypeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/product-type/{id}")
    @PreAuthorize("hasAuthority('CREATE_PRODUCT_TYPE') and hasAuthority('READ_PRODUCT_TYPE')")
    public ResponseEntity<ProductTypeDto> updateProductType(@PathVariable Long id, @RequestBody ProductTypeDto productTypeDto) {
        ProductTypeDto updatedProductTypeDto = productTypeService.update(id, productTypeDto);
        return ResponseEntity.ok(updatedProductTypeDto);
    }

    @PutMapping("/product-type/status/{id}")
    @PreAuthorize("hasAuthority('CREATE_PRODUCT_TYPE') and hasAuthority('READ_PRODUCT_TYPE')")
    public ResponseEntity<Void> updateProductTypeStatusToActive(@PathVariable Long id) {
        productTypeService.setToActiveById(id);
        return ResponseEntity.ok().build();
    }
}
