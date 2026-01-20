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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductConverter productConverter;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequestDTO productRequestDTO;
    private ProductResponseDTO productResponseDTO;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setCategory(category);
        product.setQuantity(10);
        product.setMinStockLevel(5);
        product.setPurchasePrice(new BigDecimal("1000.00"));
        product.setSalePrice(new BigDecimal("1500.00"));

        productRequestDTO = new ProductRequestDTO("Laptop", "A powerful laptop", 1L, new BigDecimal("1000.00"), new BigDecimal("1500.00"), 10, 5);
        productResponseDTO = new ProductResponseDTO(1L, "Laptop", "A powerful laptop", "Electronics", new BigDecimal("1500.00"), 10, false);
    }

    @Test
    void testFindAll() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        when(productConverter.toResponseDTO(any(Product.class))).thenReturn(productResponseDTO);

        List<ProductResponseDTO> result = productService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productConverter.toResponseDTO(any(Product.class))).thenReturn(productResponseDTO);

        ProductResponseDTO result = productService.findById(1L);

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findById(1L));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testCreate_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productConverter.toEntity(any(ProductRequestDTO.class), any(Category.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productConverter.toResponseDTO(any(Product.class))).thenReturn(productResponseDTO);

        ProductResponseDTO result = productService.create(productRequestDTO);

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testCreate_CategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.create(productRequestDTO));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testUpdate_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productConverter.toResponseDTO(any(Product.class))).thenReturn(productResponseDTO);
        
        ProductResponseDTO result = productService.update(1L, productRequestDTO);

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        verify(productRepository, times(1)).save(product);
    }
    
    @Test
    void testDecreaseStock_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        
        productService.decreaseStock(1L, 3);

        assertEquals(7, product.getQuantity());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDecreaseStock_InsufficientStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class, () -> productService.decreaseStock(1L, 11));
        verify(productRepository, never()).save(product);
    }

    @Test
    void testIncreaseStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.increaseStock(1L, 5);

        assertEquals(15, product.getQuantity());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testGetLowStockAlerts() {
        product.setQuantity(4);
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));
        when(productConverter.toResponseDTO(any(Product.class))).thenReturn(productResponseDTO);

        List<ProductResponseDTO> result = productService.getLowStockAlerts();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetLowStockAlerts_NoAlerts() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<ProductResponseDTO> result = productService.getLowStockAlerts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDelete_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.delete(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> productService.delete(1L));
        verify(productRepository, never()).deleteById(1L);
    }
}
