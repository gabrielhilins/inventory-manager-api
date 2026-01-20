package com.gabrielhenrique.small_business_inventory.dto.sales;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private BigDecimal totalValue;
    private String sellerName;
    private List<SaleItemResponseDTO> items;
}
