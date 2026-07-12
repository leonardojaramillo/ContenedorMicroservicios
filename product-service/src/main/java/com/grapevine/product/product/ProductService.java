package com.grapevine.product.product;

import com.grapevine.product.product.dto.CreateProductRequest;
import com.grapevine.product.product.dto.ProductResponse;
import com.grapevine.product.product.dto.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.grapevine.product.client.AuditClient;
import com.grapevine.product.client.dto.CreateAuditLogRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AuditClient auditClient;

    public ProductResponse create(CreateProductRequest request) {

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .volume(request.getVolume())
                .year(request.getYear())
                .imageUrl(request.getImageUrl())
                .active(true)
                .build();

        Product savedProduct = productRepository.save(product);

        try {
            auditClient.record(new CreateAuditLogRequest("PRODUCTO_CREADO", "Producto creado: " + savedProduct.getName(), null));
        } catch (Exception ignored) {}

        return mapToResponse(savedProduct);
    }

    public List<ProductResponse> findAll() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse update(Long id, UpdateProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (request.getName()        != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice()       != null) product.setPrice(request.getPrice());
        if (request.getCategory()    != null) product.setCategory(request.getCategory());
        if (request.getVolume()      != null) product.setVolume(request.getVolume());
        if (request.getYear()        != null) product.setYear(request.getYear());
        if (request.getImageUrl()    != null) product.setImageUrl(request.getImageUrl());

        Product saved = productRepository.save(product);

        try {
            auditClient.record(new CreateAuditLogRequest("PRODUCTO_ACTUALIZADO", "Producto actualizado: " + saved.getName(), null));
        } catch (Exception ignored) {}

        return mapToResponse(saved);
    }

    public ProductResponse toggleActive(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setActive(!product.getActive());

        Product saved = productRepository.save(product);

        try {
            auditClient.record(new CreateAuditLogRequest(
                    "PRODUCTO_" + (saved.getActive() ? "HABILITADO" : "INHABILITADO"),
                    "Producto " + saved.getName() + (saved.getActive() ? " habilitado" : " inhabilitado"),
                    null));
        } catch (Exception ignored) {}

        return mapToResponse(saved);
    }

    private ProductResponse mapToResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .volume(product.getVolume())
                .year(product.getYear())
                .imageUrl(product.getImageUrl())
                .active(product.getActive())
                .build();
    }

    public ProductResponse findById(Long id) {
    Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    return mapToResponse(product);
    }
}