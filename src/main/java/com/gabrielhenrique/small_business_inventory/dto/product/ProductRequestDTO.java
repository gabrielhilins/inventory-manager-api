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
public class ProductRequestDTO {
    private String name;
    private String description;
    private Long categoryId;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private Integer quantity;
    private Integer minStockLevel;
}
