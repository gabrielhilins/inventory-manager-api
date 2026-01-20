package com.gabrielhenrique.small_business_inventory.dto.sales;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemRequestDTO {
    private Long productId;
    private Integer quantity;
}
