package com.gabrielhenrique.small_business_inventory.service;

import com.gabrielhenrique.small_business_inventory.converters.ProductConverter;
import com.gabrielhenrique.small_business_inventory.dto.product.ProductRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.product.ProductResponseDTO;
import com.gabrielhenrique.small_business_inventory.exception.InsufficientStockException;
import com.gabrielhenrique.small_business_inventory.exception.ResourceNotFoundException;
import com.gabrielhenrique.small_business_inventory.model.Category;
import com.gabrielhenrique.small_business_inventory.model.Product;
import com.gabrielhenrique.small_business_inventory.repository.CategoryRepository;
import com.gabrielhenrique.small_business_inventory.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductConverter productConverter;

    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(productConverter::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO findById(Long id) {
        Product product = findEntityById(id);
        return productConverter.toResponseDTO(product);
    }

    public Product findEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public ProductResponseDTO create(ProductRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        Product product = productConverter.toEntity(dto, category);
        return productConverter.toResponseDTO(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = findEntityById(id);
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPurchasePrice(dto.getPurchasePrice());
        product.setSalePrice(dto.getSalePrice());
        product.setQuantity(dto.getQuantity());
        product.setMinStockLevel(dto.getMinStockLevel());
        product.setCategory(category);

        return productConverter.toResponseDTO(productRepository.save(product));
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Transactional
    public void decreaseStock(Long productId, Integer amount) {
        Product product = findEntityById(productId);
        if (product.getQuantity() < amount) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
        }
        product.setQuantity(product.getQuantity() - amount);
        productRepository.save(product);
    }

    @Transactional
    public void increaseStock(Long productId, Integer amount) {
        Product product = findEntityById(productId);
        product.setQuantity(product.getQuantity() + amount);
        productRepository.save(product);
    }

    public List<ProductResponseDTO> getLowStockAlerts() {
        return productRepository.findAll().stream()
                .filter(p -> p.getQuantity() <= p.getMinStockLevel())
                .map(productConverter::toResponseDTO)
                .collect(Collectors.toList());
    }
}
