package com.grapevine.finance.bank;

import com.grapevine.finance.bank.dto.BankAccountResponse;
import com.grapevine.finance.bank.dto.CreateBankAccountRequest;
import com.grapevine.finance.bank.dto.UpdateBankAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @GetMapping
    public List<BankAccountResponse> findAll() {
        return bankAccountService.findAll();
    }

    @PostMapping
    public BankAccountResponse create(@RequestBody CreateBankAccountRequest request) {
        return bankAccountService.create(request);
    }

    @PutMapping("/{id}")
    public BankAccountResponse update(@PathVariable Long id,
                                      @RequestBody UpdateBankAccountRequest request) {
        return bankAccountService.update(id, request);
    }
    @GetMapping("/{id}")
    public BankAccountResponse findById(@PathVariable Long id) {
        return bankAccountService.findById(id);
    }
    @PatchMapping("/{id}/deduct-balance")
    public BankAccountResponse deductBalance(@PathVariable Long id, @RequestParam BigDecimal amount) {
        return bankAccountService.deductBalance(id, amount);
    }

    @PatchMapping("/{id}/toggle-active")
    public BankAccountResponse toggleActive(@PathVariable Long id) {
        return bankAccountService.toggleActive(id);
    }
}