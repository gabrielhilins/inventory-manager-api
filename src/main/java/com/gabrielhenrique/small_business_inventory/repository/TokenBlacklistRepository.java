package com.gabrielhenrique.small_business_inventory.repository;

import com.gabrielhenrique.small_business_inventory.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, String> {
    Optional<TokenBlacklist> findByToken(String token);
}
