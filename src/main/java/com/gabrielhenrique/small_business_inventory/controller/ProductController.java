package com.gabrielhenrique.small_business_inventory.controller;

import com.gabrielhenrique.small_business_inventory.dto.product.ProductRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.product.ProductResponseDTO;
import com.gabrielhenrique.small_business_inventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Endpoints for managing products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Returns a list of all products.")
    public ResponseEntity<List<ProductResponseDTO>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Returns a single product by its ID.")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new product", description = "Creates a new product. Requires ADMIN role.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(data));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a product", description = "Updates an existing product. Requires ADMIN role.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id, @RequestBody @Valid ProductRequestDTO data) {
        return ResponseEntity.ok(productService.update(id, data));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a product", description = "Deletes a product. Requires ADMIN role.")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/alerts")
    @Operation(summary = "Get low stock alerts", description = "Returns a list of products that are low in stock.")
    public ResponseEntity<List<ProductResponseDTO>> getLowStock() {
        return ResponseEntity.ok(productService.getLowStockAlerts());
    }
}
