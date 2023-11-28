package com.salsel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salsel.dto.projectEnums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "product_field")
public class ProductField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer sequence;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate created_at;

    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "productField", cascade = CascadeType.ALL)
    private List<ProductFieldValues> productFieldValuesList;
}
