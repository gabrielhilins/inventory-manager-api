package com.gabrielhenrique.small_business_inventory.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String categoryName;
    private BigDecimal salePrice;
    private Integer quantity;
    private boolean lowStock;
}
