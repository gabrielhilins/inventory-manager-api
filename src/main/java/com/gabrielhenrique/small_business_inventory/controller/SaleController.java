package com.gabrielhenrique.small_business_inventory.controller;

import com.gabrielhenrique.small_business_inventory.dto.sales.SaleRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.sales.SaleResponseDTO;
import com.gabrielhenrique.small_business_inventory.model.User;
import com.gabrielhenrique.small_business_inventory.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@Tag(name = "Sales", description = "Endpoints for managing sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create a new sale", description = "Creates a new sale. Requires authentication.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<SaleResponseDTO> createSale(@RequestBody @Valid SaleRequestDTO data) {
        User seller = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.processSale(data, seller));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get sales history", description = "Returns a list of all sales. Requires authentication.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SaleResponseDTO>> getHistory() {
        return ResponseEntity.ok(saleService.findAllSales());
    }
}
