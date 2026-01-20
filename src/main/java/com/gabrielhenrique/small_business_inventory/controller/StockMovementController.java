package com.gabrielhenrique.small_business_inventory.controller;

import com.gabrielhenrique.small_business_inventory.dto.stock.StockMovementResponseDTO;
import com.gabrielhenrique.small_business_inventory.service.StockMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockMovementController {

    @Autowired
    private StockMovementService stockService;

    @GetMapping("/history/{productId}")
    public ResponseEntity<List<StockMovementResponseDTO>> getProductHistory(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.getHistoryByProduct(productId));
    }
}
