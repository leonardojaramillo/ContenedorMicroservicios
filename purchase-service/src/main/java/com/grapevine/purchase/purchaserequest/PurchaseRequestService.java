package com.grapevine.purchase.purchaserequest;

import com.grapevine.purchase.client.ProductClient;
import com.grapevine.purchase.client.dto.ProductResponse;
import com.grapevine.purchase.purchaserequest.dto.CreatePurchaseRequestDto;
import com.grapevine.purchase.purchaserequest.dto.PurchaseRequestResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseRequestService {

    private final PurchaseRequestRepository purchaseRequestRepository;
    private final ProductClient             productClient;

    public List<PurchaseRequestResponse> findAll() {
        return purchaseRequestRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).toList();
    }

    public PurchaseRequestResponse create(CreatePurchaseRequestDto dto) {
        String requestedByEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ProductResponse product = productClient.getProduct(dto.getProductId());

        PurchaseRequest request = PurchaseRequest.builder()
                .productId(product.getId())
                .productName(product.getName())
                .requestedBy(requestedByEmail)
                .quantity(dto.getQuantity())
                .justification(dto.getJustification())
                .status(PurchaseRequestStatus.PENDING)
                .purchaseCreated(false)
                .createdAt(LocalDateTime.now())
                .build();

        PurchaseRequest saved = purchaseRequestRepository.save(request);

        return toResponse(saved);
    }

    public PurchaseRequestResponse updateStatus(Long id, PurchaseRequestStatus status) {
        PurchaseRequest request = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        request.setStatus(status);
        PurchaseRequest saved = purchaseRequestRepository.save(request);

        return toResponse(saved);
    }

    public PurchaseRequestResponse markPurchaseCreated(Long id) {
        PurchaseRequest request = purchaseRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        request.setPurchaseCreated(true);
        return toResponse(purchaseRequestRepository.save(request));
    }

    private PurchaseRequestResponse toResponse(PurchaseRequest r) {
        return PurchaseRequestResponse.builder()
                .id(r.getId())
                .productName(r.getProductName())
                .requestedBy(r.getRequestedBy())
                .quantity(r.getQuantity())
                .justification(r.getJustification())
                .status(r.getStatus())
                .purchaseCreated(r.getPurchaseCreated())
                .createdAt(r.getCreatedAt())
                .build();
    }
}