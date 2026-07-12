package com.grapevine.inventory.inventory;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_adjustments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    private Integer previousStock;

    private Integer newStock;

    private String reason;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}