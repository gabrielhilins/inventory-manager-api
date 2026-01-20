package com.gabrielhenrique.small_business_inventory.service;

import com.gabrielhenrique.small_business_inventory.converters.StockMovementConverter;
import com.gabrielhenrique.small_business_inventory.dto.stock.StockMovementResponseDTO;
import com.gabrielhenrique.small_business_inventory.model.Enum.MovementType;
import com.gabrielhenrique.small_business_inventory.model.Product;
import com.gabrielhenrique.small_business_inventory.model.StockMovement;
import com.gabrielhenrique.small_business_inventory.model.User;
import com.gabrielhenrique.small_business_inventory.repository.StockMovementRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockMovementService {

    @Autowired
    private StockMovementRepository movementRepository;
    @Autowired
    private StockMovementConverter movementConverter;

    @Transactional
    public void log(Product product, Integer amount, MovementType type, String reason, User user) {
        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setAmount(amount);
        movement.setType(type);
        movement.setReason(reason);
        movement.setUser(user);
        movement.setTimestamp(LocalDateTime.now());

        movementRepository.save(movement);
    }

    public List<StockMovementResponseDTO> getHistoryByProduct(Long productId) {
        return movementRepository.findByProductIdOrderByTimestampDesc(productId).stream()
                .map(movementConverter::toResponseDTO)
                .collect(Collectors.toList());
    }
}