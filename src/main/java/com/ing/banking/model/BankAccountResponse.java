package com.ing.banking.model;

import com.ing.banking.domain.AccountType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BankAccountResponse {
    private AccountType accountType;
    private double amount;
}
