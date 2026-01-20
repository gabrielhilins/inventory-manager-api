package com.gabrielhenrique.small_business_inventory.converters;

import com.gabrielhenrique.small_business_inventory.dto.sales.SaleItemResponseDTO;
import com.gabrielhenrique.small_business_inventory.dto.sales.SaleResponseDTO;
import com.gabrielhenrique.small_business_inventory.model.Sale;
import com.gabrielhenrique.small_business_inventory.model.SaleItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SaleConverter {

    public SaleResponseDTO toResponseDTO(Sale sale) {
        SaleResponseDTO dto = new SaleResponseDTO();
        dto.setId(sale.getId());
        dto.setCreatedAt(sale.getCreatedAt());
        dto.setTotalValue(sale.getTotalValue());
        dto.setSellerName(sale.getSeller().getName());
        List<SaleItemResponseDTO> itemDTOs = sale.getItems().stream()
                .map(this::toItemResponseDTO)
                .collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }

    private SaleItemResponseDTO toItemResponseDTO(SaleItem item) {
        SaleItemResponseDTO dto = new SaleItemResponseDTO();
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPriceAtTime(item.getPriceAtTime());
        dto.setSubtotal(item.getPriceAtTime().multiply(BigDecimal.valueOf(item.getQuantity())));
        return dto;
    }
}
