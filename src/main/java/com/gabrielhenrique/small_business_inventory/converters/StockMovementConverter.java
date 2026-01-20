package com.gabrielhenrique.small_business_inventory.converters;

import com.gabrielhenrique.small_business_inventory.dto.stock.StockMovementResponseDTO;
import com.gabrielhenrique.small_business_inventory.model.StockMovement;
import org.springframework.stereotype.Component;

@Component
public class StockMovementConverter {

    public StockMovementResponseDTO toResponseDTO(StockMovement movement) {
        StockMovementResponseDTO dto = new StockMovementResponseDTO();
        dto.setId(movement.getId());
        dto.setProductName(movement.getProduct().getName());
        dto.setType(movement.getType().name());
        dto.setAmount(movement.getAmount());
        dto.setReason(movement.getReason());
        dto.setTimestamp(movement.getTimestamp());
        dto.setUserName(movement.getUser().getName());
        return dto;
    }
}
