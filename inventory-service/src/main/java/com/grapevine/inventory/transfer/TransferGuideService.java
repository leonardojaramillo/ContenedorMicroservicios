package com.grapevine.inventory.transfer;

import com.grapevine.inventory.client.ProductClient;
import com.grapevine.inventory.client.dto.ProductResponse;
import com.grapevine.inventory.client.dto.WarehouseResponse;
import com.grapevine.inventory.transfer.dto.*;
import com.grapevine.inventory.warehouse_stock.WarehouseStock;
import com.grapevine.inventory.warehouse_stock.WarehouseStockRepository;
import com.grapevine.inventory.client.AuditClient;
import com.grapevine.inventory.client.dto.CreateAuditLogRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferGuideService {

    private final TransferGuideRepository  transferGuideRepository;
    private final WarehouseStockRepository warehouseStockRepository;
    private final ProductClient            productClient;
    private final AuditClient auditClient;

    public List<TransferGuideResponse> findAll() {
        return transferGuideRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).toList();
    }

    public TransferGuideResponse findById(Long id) {
        return toResponse(getOrThrow(id));
    }

    public TransferGuideResponse create(CreateTransferGuideRequest request) {
        String createdByEmail = currentUserEmail();

        TransferGuide guide = TransferGuide.builder()
                .type(request.getType())
                .status(TransferGuideStatus.BORRADOR)
                .originWarehouseId(request.getOriginWarehouseId())
                .destinationWarehouseId(request.getDestinationWarehouseId())
                .description(request.getDescription())
                .createdBy(createdByEmail)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        if (request.getItems() != null) {
            List<TransferGuideItem> items = request.getItems().stream()
                    .map(i -> {
                        ProductResponse product = productClient.getProduct(i.getProductId());
                        return TransferGuideItem.builder()
                                .guide(guide)
                                .productId(product.getId())
                                .productName(product.getName())
                                .quantity(i.getQuantity())
                                .build();
                    })
                    .toList();
            guide.setItems(items);
        }

        TransferGuide saved = transferGuideRepository.save(guide);

        try {
            auditClient.record(new CreateAuditLogRequest("GUIA_CREADA", "Guía #" + saved.getId() + " (" + saved.getType() + ")", createdByEmail));
        } catch (Exception ignored) {}

        return toResponse(saved);
    }

    @Transactional
    public TransferGuideResponse update(Long id, CreateTransferGuideRequest request) {
        TransferGuide g = getOrThrow(id);
        assertStatus(g, TransferGuideStatus.BORRADOR);

        g.setType(request.getType());
        g.setOriginWarehouseId(request.getOriginWarehouseId());
        g.setDestinationWarehouseId(request.getDestinationWarehouseId());
        g.setDescription(request.getDescription());
        g.setUpdatedAt(LocalDateTime.now());

        g.getItems().clear();

        if (request.getItems() != null) {
            List<TransferGuideItem> items = request.getItems().stream()
                    .map(i -> {
                        ProductResponse product = productClient.getProduct(i.getProductId());
                        return TransferGuideItem.builder()
                                .guide(g)
                                .productId(product.getId())
                                .productName(product.getName())
                                .quantity(i.getQuantity())
                                .build();
                    })
                    .toList();
            g.getItems().addAll(items);
        }

        TransferGuide saved = transferGuideRepository.save(g);

        try {
            auditClient.record(new CreateAuditLogRequest("GUIA_ACTUALIZADA", "Guía #" + saved.getId() + " editada", null));
        } catch (Exception ignored) {}

        return toResponse(saved);
    }

    public void delete(Long id) {
        TransferGuide g = getOrThrow(id);
        assertStatus(g, TransferGuideStatus.BORRADOR);
        try {
            auditClient.record(new CreateAuditLogRequest("GUIA_ELIMINADA", "Guía #" + g.getId() + " eliminada", null));
        } catch (Exception ignored) {}

        transferGuideRepository.delete(g);
    }

    public TransferGuideResponse prepare(Long id) {
        TransferGuide g = getOrThrow(id);
        assertStatus(g, TransferGuideStatus.BORRADOR);
        g.setStatus(TransferGuideStatus.PREPARANDO);
        g.setUpdatedAt(LocalDateTime.now());
        return toResponse(transferGuideRepository.save(g));
    }

    @Transactional
    public TransferGuideResponse dispatch(Long id) {
        TransferGuide g = getOrThrow(id);
        assertStatus(g, TransferGuideStatus.PREPARANDO);

        if (g.getOriginWarehouseId() != null) {
            deductStockFromOrigin(g);
        }

        g.setStatus(TransferGuideStatus.EN_TRANSITO);
        g.setUpdatedAt(LocalDateTime.now());
        try {
            auditClient.record(new CreateAuditLogRequest("GUIA_" + g.getStatus(), "Guía #" + g.getId() + " → " + g.getStatus(), null));
        } catch (Exception ignored) {}

        return toResponse(transferGuideRepository.save(g));
    }

    @Transactional
    public TransferGuideResponse deliver(Long id) {
        TransferGuide g = getOrThrow(id);
        if (g.getStatus() != TransferGuideStatus.EN_TRANSITO
                && g.getStatus() != TransferGuideStatus.INCIDENCIA) {
            throw new RuntimeException("Estado inválido para marcar como entregado");
        }

        if (g.getDestinationWarehouseId() != null) {
            addStockToDestination(g);
        }

        g.setStatus(TransferGuideStatus.ENTREGADO);
        g.setUpdatedAt(LocalDateTime.now());
        try {
            auditClient.record(new CreateAuditLogRequest("GUIA_" + g.getStatus(), "Guía #" + g.getId() + " → " + g.getStatus(), null));
        } catch (Exception ignored) {}

        return toResponse(transferGuideRepository.save(g));
    }

    public TransferGuideResponse reportIncident(Long id, IncidentRequest request) {
        TransferGuide g = getOrThrow(id);
        assertStatus(g, TransferGuideStatus.EN_TRANSITO);
        g.setStatus(TransferGuideStatus.INCIDENCIA);
        g.setIncidentReason(request.getReason());
        g.setIncidentEvidenceUrl(request.getEvidenceUrl());
        g.setStockRecoverable(request.getStockRecoverable());
        g.setUpdatedAt(LocalDateTime.now());
        try {
            auditClient.record(new CreateAuditLogRequest("GUIA_" + g.getStatus(), "Guía #" + g.getId() + " → " + g.getStatus(), null));
        } catch (Exception ignored) {}

        return toResponse(transferGuideRepository.save(g));
    }

    public TransferGuideResponse resume(Long id) {
        TransferGuide g = getOrThrow(id);
        assertStatus(g, TransferGuideStatus.INCIDENCIA);
        g.setStatus(TransferGuideStatus.EN_TRANSITO);
        g.setUpdatedAt(LocalDateTime.now());
        try {
            auditClient.record(new CreateAuditLogRequest("GUIA_" + g.getStatus(), "Guía #" + g.getId() + " → " + g.getStatus(), null));
        } catch (Exception ignored) {}

        return toResponse(transferGuideRepository.save(g));
    }

    @Transactional
    public TransferGuideResponse cancel(Long id) {
        TransferGuide g = getOrThrow(id);
        assertStatus(g, TransferGuideStatus.INCIDENCIA);

        if (Boolean.TRUE.equals(g.getStockRecoverable()) && g.getOriginWarehouseId() != null) {
            restoreStockToOrigin(g);
        }

        g.setStatus(TransferGuideStatus.CANCELADO);
        g.setUpdatedAt(LocalDateTime.now());
        try {
            auditClient.record(new CreateAuditLogRequest("GUIA_" + g.getStatus(), "Guía #" + g.getId() + " → " + g.getStatus(), null));
        } catch (Exception ignored) {}

        return toResponse(transferGuideRepository.save(g));
    }

    private void deductStockFromOrigin(TransferGuide g) {
        for (TransferGuideItem item : g.getItems()) {
            WarehouseStock ws = warehouseStockRepository
                    .findByWarehouseIdAndProductId(g.getOriginWarehouseId(), item.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Stock insuficiente en origen para: " + item.getProductName()));
            if (ws.getStock() < item.getQuantity()) {
                throw new RuntimeException(
                        "Stock insuficiente para: " + item.getProductName()
                                + " (disponible: " + ws.getStock() + ")");
            }
            ws.setStock(ws.getStock() - item.getQuantity());
            warehouseStockRepository.save(ws);
        }
    }

    private void addStockToDestination(TransferGuide g) {
        for (TransferGuideItem item : g.getItems()) {
            WarehouseStock ws = warehouseStockRepository
                    .findByWarehouseIdAndProductId(g.getDestinationWarehouseId(), item.getProductId())
                    .orElse(WarehouseStock.builder()
                            .warehouseId(g.getDestinationWarehouseId())
                            .productId(item.getProductId())
                            .stock(0)
                            .build());
            ws.setStock(ws.getStock() + item.getQuantity());
            warehouseStockRepository.save(ws);
        }
    }

    private void restoreStockToOrigin(TransferGuide g) {
        for (TransferGuideItem item : g.getItems()) {
            WarehouseStock ws = warehouseStockRepository
                    .findByWarehouseIdAndProductId(g.getOriginWarehouseId(), item.getProductId())
                    .orElse(WarehouseStock.builder()
                            .warehouseId(g.getOriginWarehouseId())
                            .productId(item.getProductId())
                            .stock(0)
                            .build());
            ws.setStock(ws.getStock() + item.getQuantity());
            warehouseStockRepository.save(ws);
        }
    }

    private TransferGuide getOrThrow(Long id) {
        return transferGuideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada"));
    }

    private void assertStatus(TransferGuide g, TransferGuideStatus expected) {
        if (g.getStatus() != expected) {
            throw new RuntimeException(
                    "Estado inválido. Se esperaba " + expected + " pero es " + g.getStatus());
        }
    }

    private String currentUserEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private String safeWarehouseName(Long warehouseId) {
        if (warehouseId == null) return "—";
        try {
            WarehouseResponse w = productClient.getWarehouse(warehouseId);
            return w.getName();
        } catch (Exception e) {
            return "Almacén no disponible";
        }
    }

    private TransferGuideResponse toResponse(TransferGuide g) {
        return TransferGuideResponse.builder()
                .id(g.getId())
                .type(g.getType())
                .status(g.getStatus())
                .originWarehouse(safeWarehouseName(g.getOriginWarehouseId()))
                .originWarehouseId(g.getOriginWarehouseId())
                .destinationWarehouse(safeWarehouseName(g.getDestinationWarehouseId()))
                .destinationWarehouseId(g.getDestinationWarehouseId())
                .description(g.getDescription())
                .createdBy(g.getCreatedBy())
                .createdAt(g.getCreatedAt())
                .updatedAt(g.getUpdatedAt())
                .incidentReason(g.getIncidentReason())
                .incidentEvidenceUrl(g.getIncidentEvidenceUrl())
                .stockRecoverable(g.getStockRecoverable())
                .items(g.getItems() != null ? g.getItems().stream()
                                              .map(i -> TransferGuideItemResponse.builder()
                                                        .productId(i.getProductId())
                                                        .productName(i.getProductName())
                                                        .quantity(i.getQuantity())
                                                        .build())
                                              .toList() : List.of())
                .build();
    }
}