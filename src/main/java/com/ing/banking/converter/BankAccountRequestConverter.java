package com.ing.banking.converter;

import com.ing.banking.domain.AccountType;
import com.ing.banking.domain.BankAccount;
import com.ing.banking.domain.User;
import org.springframework.stereotype.Component;

@Component
public class BankAccountRequestConverter {

    public BankAccount convert(String accountType, User user) {
        return BankAccount.builder()
                .amount(0)
                .user(user)
                .accountType(AccountType.valueOf(accountType))
                .build();
    }
}
