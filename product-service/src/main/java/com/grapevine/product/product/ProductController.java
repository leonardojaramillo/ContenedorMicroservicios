package com.grapevine.product.product;

import com.grapevine.product.product.dto.CreateProductRequest;
import com.grapevine.product.product.dto.ProductResponse;
import com.grapevine.product.product.dto.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public ProductResponse create(@RequestBody CreateProductRequest request) {
        return productService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CAJERO', 'VENDEDOR', 'LOGISTICA', 'SOFTWARE_ENGINEER')")
    public List<ProductResponse> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CAJERO', 'VENDEDOR', 'LOGISTICA', 'SOFTWARE_ENGINEER')")
    public ProductResponse findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public ProductResponse update(@PathVariable Long id,
                                  @RequestBody UpdateProductRequest request) {
        return productService.update(id, request);
    }

    @PatchMapping("/{id}/toggle-active")
    @PreAuthorize("hasAnyRole('LOGISTICA', 'SOFTWARE_ENGINEER')")
    public ProductResponse toggleActive(@PathVariable Long id) {
        return productService.toggleActive(id);
    }
}