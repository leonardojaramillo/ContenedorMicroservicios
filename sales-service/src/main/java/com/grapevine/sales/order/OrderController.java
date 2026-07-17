package com.grapevine.sales.order;

import com.grapevine.sales.order.dto.CancelOrderRequest;
import com.grapevine.sales.order.dto.CreateOrderRequest;
import com.grapevine.sales.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('VENDEDOR', 'SOFTWARE_ENGINEER')")
    public OrderResponse create(@RequestBody CreateOrderRequest request) {
        return orderService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'CAJERO', 'SOFTWARE_ENGINEER')")
    public List<OrderResponse> findAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VENDEDOR', 'CAJERO', 'SOFTWARE_ENGINEER')")
    public OrderResponse findById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping("/customer/{document}")
    @PreAuthorize("hasAnyRole('VENDEDOR', 'SOFTWARE_ENGINEER')")
    public List<OrderResponse> findByCustomerDocument(@PathVariable String document) {
        return orderService.findByCustomerDocument(document);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'SOFTWARE_ENGINEER')")
    public OrderResponse cancel(@PathVariable Long id, @RequestBody CancelOrderRequest request) {
        return orderService.cancel(id, request.getReason());
    }
}