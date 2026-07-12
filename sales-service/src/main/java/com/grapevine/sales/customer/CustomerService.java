package com.grapevine.sales.customer;

import com.grapevine.sales.customer.dto.CreateCustomerRequest;
import com.grapevine.sales.customer.dto.CustomerResponse;
import com.grapevine.sales.customer.dto.UpdateCustomerRequest;
import com.grapevine.sales.client.AuditClient;
import com.grapevine.sales.client.dto.CreateAuditLogRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AuditClient auditClient;

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CustomerResponse create(CreateCustomerRequest request) {
        Customer customer = Customer.builder()
                .razonSocial(request.getRazonSocial())
                .tipoDocumento(request.getTipoDocumento())
                .documento(request.getDocumento())
                .contacto(request.getContacto())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .segmento(request.getSegmento())
                .active(true)
                .build();

        Customer saved = customerRepository.save(customer);
        try {
            auditClient.record(new CreateAuditLogRequest("CLIENTE_CREADO", "Cliente creado: " + saved.getRazonSocial(), null));
        } catch (Exception ignored) {}

        return toResponse(saved);
    }

    public CustomerResponse update(Long id, UpdateCustomerRequest request) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (request.getRazonSocial()   != null) c.setRazonSocial(request.getRazonSocial());
        if (request.getTipoDocumento() != null) c.setTipoDocumento(request.getTipoDocumento());
        if (request.getDocumento()     != null) c.setDocumento(request.getDocumento());
        if (request.getContacto()      != null) c.setContacto(request.getContacto());
        if (request.getTelefono()      != null) c.setTelefono(request.getTelefono());
        if (request.getEmail()         != null) c.setEmail(request.getEmail());
        if (request.getSegmento()      != null) c.setSegmento(request.getSegmento());

        Customer saved = customerRepository.save(c);

        try {
            auditClient.record(new CreateAuditLogRequest("CLIENTE_ACTUALIZADO", "Cliente actualizado: " + saved.getRazonSocial(), null));
        } catch (Exception ignored) {}

        return toResponse(saved);
    }

    public CustomerResponse toggleActive(Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        c.setActive(!c.getActive());
        Customer saved = customerRepository.save(c);

        try {
            auditClient.record(new CreateAuditLogRequest(
                    "CLIENTE_" + (saved.getActive() ? "HABILITADO" : "INHABILITADO"),
                    "Cliente " + saved.getRazonSocial() + (saved.getActive() ? " habilitado" : " inhabilitado"),
                    null));
        } catch (Exception ignored) {}

        return toResponse(saved);
    }

    private CustomerResponse toResponse(Customer c) {
        return CustomerResponse.builder()
                .id(c.getId())
                .razonSocial(c.getRazonSocial())
                .tipoDocumento(c.getTipoDocumento())
                .documento(c.getDocumento())
                .contacto(c.getContacto())
                .telefono(c.getTelefono())
                .email(c.getEmail())
                .segmento(c.getSegmento())
                .active(c.getActive())
                .build();
    }
}