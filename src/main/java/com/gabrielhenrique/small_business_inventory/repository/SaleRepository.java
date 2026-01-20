package com.gabrielhenrique.small_business_inventory.repository;

import com.gabrielhenrique.small_business_inventory.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<Sale> findBySellerId(Long sellerId);
    List<Sale> findTop10ByOrderByCreatedAtDesc();
}
