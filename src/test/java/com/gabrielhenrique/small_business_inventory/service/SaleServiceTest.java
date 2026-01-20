package com.gabrielhenrique.small_business_inventory.service;

import com.gabrielhenrique.small_business_inventory.converters.SaleConverter;
import com.gabrielhenrique.small_business_inventory.dto.sales.SaleItemRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.sales.SaleRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.sales.SaleResponseDTO;
import com.gabrielhenrique.small_business_inventory.exception.InsufficientStockException;
import com.gabrielhenrique.small_business_inventory.model.Enum.MovementType;
import com.gabrielhenrique.small_business_inventory.model.Product;
import com.gabrielhenrique.small_business_inventory.model.Sale;
import com.gabrielhenrique.small_business_inventory.model.User;
import com.gabrielhenrique.small_business_inventory.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ProductService productService;

    @Mock
    private StockMovementService stockMovementService;

    @Mock
    private SaleConverter saleConverter;

    @InjectMocks
    private SaleService saleService;

    private User seller;
    private Product product;
    private SaleRequestDTO saleRequestDTO;
    private Sale sale;
    private SaleResponseDTO saleResponseDTO;

    @BeforeEach
    void setUp() {
        seller = new User();
        seller.setId(1L);
        seller.setName("Seller");

        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setSalePrice(new BigDecimal("1500.00"));
        product.setQuantity(10);

        SaleItemRequestDTO itemDTO = new SaleItemRequestDTO(1L, 2);
        saleRequestDTO = new SaleRequestDTO(Collections.singletonList(itemDTO));

        sale = new Sale();
        sale.setId(1L);
        sale.setSeller(seller);
        sale.setTotalValue(new BigDecimal("3000.00"));

        saleResponseDTO = new SaleResponseDTO();
        saleResponseDTO.setId(1L);
        saleResponseDTO.setTotalValue(new BigDecimal("3000.00"));
    }

    @Test
    void testProcessSale_Success() {
        when(productService.findEntityById(1L)).thenReturn(product);
        doNothing().when(productService).decreaseStock(1L, 2);
        doNothing().when(stockMovementService).log(any(Product.class), anyInt(), any(MovementType.class), anyString(), any(User.class));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);
        when(saleConverter.toResponseDTO(any(Sale.class))).thenReturn(saleResponseDTO);

        SaleResponseDTO result = saleService.processSale(saleRequestDTO, seller);

        assertNotNull(result);
        assertEquals(sale.getId(), result.getId());
        verify(productService, times(1)).decreaseStock(1L, 2);
        verify(stockMovementService, times(1)).log(any(Product.class), anyInt(), any(MovementType.class), anyString(), any(User.class));
        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    void testProcessSale_InsufficientStock() {
        when(productService.findEntityById(1L)).thenReturn(product);
        doThrow(new InsufficientStockException("")).when(productService).decreaseStock(anyLong(), anyInt());

        assertThrows(InsufficientStockException.class, () -> {
            saleService.processSale(saleRequestDTO, seller);
        });

        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void testFindAllSales() {
        when(saleRepository.findAll()).thenReturn(Collections.singletonList(sale));
        when(saleConverter.toResponseDTO(any(Sale.class))).thenReturn(saleResponseDTO);

        List<SaleResponseDTO> result = saleService.findAllSales();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(saleRepository, times(1)).findAll();
    }
}
