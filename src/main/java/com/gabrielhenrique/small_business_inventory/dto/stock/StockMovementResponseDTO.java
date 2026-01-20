package com.gabrielhenrique.small_business_inventory.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StockMovementResponseDTO {
    private Long id;
    private String productName;
    private String type;
    private Integer amount;
    private String reason;
    private LocalDateTime timestamp;
    private String userName;
}
