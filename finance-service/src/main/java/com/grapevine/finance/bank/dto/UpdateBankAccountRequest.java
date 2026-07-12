package com.grapevine.finance.bank.dto;

import com.grapevine.finance.bank.AccountType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBankAccountRequest {

    private String accountName;
    private String bank;
    private String accountNumber;
    private AccountType type;
    private String currency;
}