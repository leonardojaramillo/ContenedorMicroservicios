package com.grapevine.product.warehouse;

import com.grapevine.product.warehouse.dto.CreateWarehouseRequest;
import com.grapevine.product.warehouse.dto.UpdateWarehouseRequest;
import com.grapevine.product.warehouse.dto.WarehouseResponse;
import com.grapevine.product.client.AuditClient;
import com.grapevine.product.client.dto.CreateAuditLogRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository     warehouseRepository;
    private final AuditClient auditClient;

    public WarehouseResponse create(CreateWarehouseRequest request) {
        Warehouse w = Warehouse.builder()
                .name(request.getName())
                .address(request.getAddress())
                .ubigeoCode(request.getUbigeoCode())
                .department(request.getDepartment())
                .province(request.getProvince())
                .district(request.getDistrict())
                .active(true)
                .build();
        Warehouse saved = warehouseRepository.save(w);

        try {
            auditClient.record(new CreateAuditLogRequest("ALMACEN_CREADO", "Almacén creado: " + saved.getName(), null));
        } catch (Exception ignored) {}

        return toResponse(saved);
    }

    public List<WarehouseResponse> findAll() {
        return warehouseRepository.findAll().stream().map(this::toResponse).toList();
    }

    public WarehouseResponse update(Long id, UpdateWarehouseRequest request) {
        Warehouse w = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));
        if (request.getName()       != null) w.setName(request.getName());
        if (request.getAddress()    != null) w.setAddress(request.getAddress());
        if (request.getUbigeoCode() != null) w.setUbigeoCode(request.getUbigeoCode());
        if (request.getDepartment() != null) w.setDepartment(request.getDepartment());
        if (request.getProvince()   != null) w.setProvince(request.getProvince());
        if (request.getDistrict()   != null) w.setDistrict(request.getDistrict());

        Warehouse saved = warehouseRepository.save(w);

        try {
            auditClient.record(new CreateAuditLogRequest("ALMACEN_ACTUALIZADO", "Almacén actualizado: " + saved.getName(), null));
        } catch (Exception ignored) {}

        return toResponse(saved);
    }

    public WarehouseResponse toggleActive(Long id) {
        Warehouse w = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));
        w.setActive(!w.getActive());
        Warehouse saved = warehouseRepository.save(w);

        try {
            auditClient.record(new CreateAuditLogRequest(
                    "ALMACEN_" + (saved.getActive() ? "HABILITADO" : "INHABILITADO"),
                    "Almacén " + saved.getName() + (saved.getActive() ? " habilitado" : " inhabilitado"),
                    null));
        } catch (Exception ignored) {}


        return toResponse(saved);
    }

    public WarehouseResponse findById(Long id) {
        Warehouse w = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));
        return toResponse(w);
    }

    private WarehouseResponse toResponse(Warehouse w) {
        return WarehouseResponse.builder()
                .id(w.getId())
                .name(w.getName())
                .address(w.getAddress())
                .active(w.getActive())
                .ubigeoCode(w.getUbigeoCode())
                .department(w.getDepartment())
                .province(w.getProvince())
                .district(w.getDistrict())
                .build();
    }
}