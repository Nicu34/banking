package com.ing.banking.converter;

import com.ing.banking.domain.BankAccount;
import com.ing.banking.model.BankAccountResponse;
import org.springframework.stereotype.Component;

@Component
public class BankAccountResponseConverter extends GenericConverter<BankAccount, BankAccountResponse> {

    @Override
    public BankAccountResponse convert(BankAccount source) {
        return BankAccountResponse.builder()
                .accountType(source.getAccountType())
                .amount(source.getAmount())
                .build();
    }
}
