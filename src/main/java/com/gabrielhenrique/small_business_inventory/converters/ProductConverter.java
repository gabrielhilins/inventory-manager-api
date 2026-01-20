package com.gabrielhenrique.small_business_inventory.converters;

import com.gabrielhenrique.small_business_inventory.dto.product.ProductRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.product.ProductResponseDTO;
import com.gabrielhenrique.small_business_inventory.model.Category;
import com.gabrielhenrique.small_business_inventory.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {

    public ProductResponseDTO toResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategoryName(product.getCategory().getName());
        dto.setSalePrice(product.getSalePrice());
        dto.setQuantity(product.getQuantity());
        dto.setLowStock(product.getQuantity() <= product.getMinStockLevel());
        return dto;
    }

    public Product toEntity(ProductRequestDTO dto, Category category) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPurchasePrice(dto.getPurchasePrice());
        product.setSalePrice(dto.getSalePrice());
        product.setQuantity(dto.getQuantity());
        product.setMinStockLevel(dto.getMinStockLevel());
        product.setCategory(category);
        return product;
    }
}
