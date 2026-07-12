package com.grapevine.inventory.transfer;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transfer_guides")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferGuide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferGuideStatus status;

    @Column(name = "origin_warehouse_id")
    private Long originWarehouseId;

    @Column(name = "destination_warehouse_id")
    private Long destinationWarehouseId;

    private String description;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String  incidentReason;
    private String  incidentEvidenceUrl;
    private Boolean stockRecoverable;

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferGuideItem> items;
}