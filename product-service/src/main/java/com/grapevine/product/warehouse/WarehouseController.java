package com.grapevine.product.warehouse;

import com.grapevine.product.warehouse.dto.CreateWarehouseRequest;
import com.grapevine.product.warehouse.dto.UpdateWarehouseRequest;
import com.grapevine.product.warehouse.dto.WarehouseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public WarehouseResponse create(@RequestBody CreateWarehouseRequest request) {
        return warehouseService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public List<WarehouseResponse> findAll() {
        return warehouseService.findAll();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public WarehouseResponse update(@PathVariable Long id,
                                    @RequestBody UpdateWarehouseRequest request) {
        return warehouseService.update(id, request);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public WarehouseResponse findById(@PathVariable Long id) {
        return warehouseService.findById(id);
    }

    @PatchMapping("/{id}/toggle-active")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public WarehouseResponse toggleActive(@PathVariable Long id) {
        return warehouseService.toggleActive(id);
    }

}
