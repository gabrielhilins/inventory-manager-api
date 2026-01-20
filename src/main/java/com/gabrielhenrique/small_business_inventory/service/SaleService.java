package com.gabrielhenrique.small_business_inventory.service;

import com.gabrielhenrique.small_business_inventory.converters.SaleConverter;
import com.gabrielhenrique.small_business_inventory.dto.sales.SaleItemRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.sales.SaleRequestDTO;
import com.gabrielhenrique.small_business_inventory.dto.sales.SaleResponseDTO;
import com.gabrielhenrique.small_business_inventory.model.Enum.MovementType;
import com.gabrielhenrique.small_business_inventory.model.Product;
import com.gabrielhenrique.small_business_inventory.model.Sale;
import com.gabrielhenrique.small_business_inventory.model.SaleItem;
import com.gabrielhenrique.small_business_inventory.model.User;
import com.gabrielhenrique.small_business_inventory.repository.SaleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private StockMovementService stockService;
    @Autowired
    private SaleConverter saleConverter;

    @Transactional
    public SaleResponseDTO processSale(SaleRequestDTO dto, User seller) {
        Sale sale = new Sale();
        sale.setSeller(seller);
        sale.setCreatedAt(LocalDateTime.now());

        BigDecimal totalValue = BigDecimal.ZERO;

        for (SaleItemRequestDTO itemDto : dto.getItems()) {
            Product product = productService.findEntityById(itemDto.getProductId());

            // Valida e baixa estoque
            productService.decreaseStock(product.getId(), itemDto.getQuantity());

            // Cria o item da venda
            SaleItem item = new SaleItem();
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setPriceAtTime(product.getSalePrice());
            item.setSale(sale);

            // Calcula financeiro
            BigDecimal subtotal = product.getSalePrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            totalValue = totalValue.add(subtotal);

            // Registra auditoria de saída
            stockService.log(product, itemDto.getQuantity(), MovementType.OUTGOING, "SALE #" + sale.getId(), seller);

            sale.getItems().add(item);
        }

        sale.setTotalValue(totalValue);
        Sale savedSale = saleRepository.save(sale);

        return saleConverter.toResponseDTO(savedSale);
    }

    public List<SaleResponseDTO> findAllSales() {
        return saleRepository.findAll().stream()
                .map(saleConverter::toResponseDTO)
                .collect(Collectors.toList());
    }
}