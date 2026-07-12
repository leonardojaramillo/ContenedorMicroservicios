package com.grapevine.sales.customer;

import com.grapevine.sales.customer.dto.CreateCustomerRequest;
import com.grapevine.sales.customer.dto.CustomerResponse;
import com.grapevine.sales.customer.dto.UpdateCustomerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasAnyRole('VENDEDOR', 'SOFTWARE_ENGINEER')")
    public List<CustomerResponse> findAll() {
        return customerService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('VENDEDOR', 'SOFTWARE_ENGINEER')")
    public CustomerResponse create(@RequestBody CreateCustomerRequest request) {
        return customerService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('VENDEDOR', 'SOFTWARE_ENGINEER')")
    public CustomerResponse update(@PathVariable Long id,
                                   @RequestBody UpdateCustomerRequest request) {
        return customerService.update(id, request);
    }

    @PatchMapping("/{id}/toggle-active")
    @PreAuthorize("hasAnyRole('VENDEDOR', 'SOFTWARE_ENGINEER')")
    public CustomerResponse toggleActive(@PathVariable Long id) {
        return customerService.toggleActive(id);
    }
}