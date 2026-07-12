package com.grapevine.purchase.supplier;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.grapevine.purchase.client.AuditClient;
import com.grapevine.purchase.client.dto.CreateAuditLogRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final AuditClient auditClient;

    public Supplier create(Supplier supplier) {
        supplier.setActive(true);
        Supplier saved = supplierRepository.save(supplier);

        try {
            auditClient.record(new CreateAuditLogRequest("PROVEEDOR_CREADO", "Proveedor creado: " + saved.getName(), null));
        } catch (Exception ignored) {}

        return saved;
    }

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    public Supplier toggleActive(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        supplier.setActive(!supplier.getActive());
        Supplier saved = supplierRepository.save(supplier);

        try {
            auditClient.record(new CreateAuditLogRequest(
                    "PROVEEDOR_" + (saved.getActive() ? "HABILITADO" : "INHABILITADO"),
                    "Proveedor " + saved.getName() + (saved.getActive() ? " habilitado" : " inhabilitado"),
                    null));
        } catch (Exception ignored) {}

        return saved;
    }
}