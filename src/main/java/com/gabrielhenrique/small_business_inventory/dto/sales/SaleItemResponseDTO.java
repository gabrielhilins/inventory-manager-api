package com.gabrielhenrique.small_business_inventory.dto.sales;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemResponseDTO {
    private String productName;
    private Integer quantity;
    private BigDecimal priceAtTime;
    private BigDecimal subtotal;
}
