package com.gabrielhenrique.small_business_inventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    @Column
    private String description;
    @Column
    private BigDecimal purchasePrice;
    @Column
    private BigDecimal salePrice;
    @Column
    private Integer quantity;
    @Column
    private Integer minStockLevel;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<SaleItem> saleItems = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<StockMovement> movements = new ArrayList<>();
    
}
