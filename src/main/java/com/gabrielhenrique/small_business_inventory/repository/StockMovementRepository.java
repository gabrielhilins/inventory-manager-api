package com.gabrielhenrique.small_business_inventory.repository;

import com.gabrielhenrique.small_business_inventory.model.Enum.MovementType;
import com.gabrielhenrique.small_business_inventory.model.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByProductIdOrderByTimestampDesc(Long productId);
    List<StockMovement> findByType(MovementType type);
    List<StockMovement> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
