package com.gabrielhenrique.small_business_inventory.model;

import com.gabrielhenrique.small_business_inventory.model.Enum.MovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movement")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer amount;

    @Column
    private String reason;

    @Column
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column
    private MovementType type;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
