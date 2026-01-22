package com.gabrielhenrique.small_business_inventory.controller;

import com.gabrielhenrique.small_business_inventory.dto.stock.StockMovementResponseDTO;
import com.gabrielhenrique.small_business_inventory.service.StockMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@Tag(name = "Stock", description = "Endpoints for managing stock")
public class StockMovementController {

    private final StockMovementService stockService;

    public StockMovementController(StockMovementService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/history/{productId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get stock history for a product", description = "Returns the stock movement history for a specific product. Requires authentication.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<StockMovementResponseDTO>> getProductHistory(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getHistoryByProduct(productId));
    }
}
