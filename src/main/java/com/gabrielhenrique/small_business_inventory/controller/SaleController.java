package com.gabrielhenrique.small_business_inventory.controller;

import com.gabrielhenrique.small_business_inventory.dto.sales.SaleRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.sales.SaleResponseDTO;
import com.gabrielhenrique.small_business_inventory.model.User;
import com.gabrielhenrique.small_business_inventory.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping
    public ResponseEntity<SaleResponseDTO> createSale(@RequestBody @Valid SaleRequestDTO data) {
        // Recupera o usuário autenticado no Token JWT via Contexto do Spring Security
        User seller = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.processSale(data, seller));
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDTO>> getHistory() {
        // Implemente um método no SaleService para buscar todas as vendas se desejar
        return ResponseEntity.ok(saleService.findAllSales());
    }
}
